package net.bzk.infrastructure.tscurve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;


public class PeakFinder {

    public enum Direction {
        RISE, FALL
    }

    public enum PointType {
        MINED, MAXED, NONE
    }

    public enum Dimension {
        MACRO, MICRO
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Point {
        private int idx;
        private String key;
        private double val;
        private double dtime;
    }

    @Data
    public static class MinMaxInfo {
        private List<Point> min = new ArrayList<>();
        private List<Point> max = new ArrayList<>();
        private List<Point> all = new ArrayList<>();
    }

    @Data
    public static class Result {
        private TrendInfo micro;
        private TrendInfo macro;
        private LastInfo last;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LastInfo {
        private String time;
        private double val;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TrendInfo {
        private Direction state;
        private Map<Double, Point> maxList;
        private Map<Double, Point> minList;
        private Map<Double, Point> allList;
        private double maxNearTime;
        private double minNearTime;
        private Point nearMax;
        private Point nearMin;
        private Point nearPeak;
    }


    private final Map<String, Double> rMap;
    private final double baseVal;
    private final double peakMaxWaitSeconds;
    private final double macroAmplitudeRate;
    private final List<String> keys;
    private final String firstKey;
    private final MacroAmplitudeFilter macroAmplitudeFilter = new MacroAmplitudeFilter();


    public PeakFinder(Map<String, Double> rm, double baseVal, double peakMaxWaitSeconds, double macroAmplitudeRate) {
        rMap = rm;
        this.baseVal = baseVal;
        this.peakMaxWaitSeconds = peakMaxWaitSeconds;
        this.macroAmplitudeRate = macroAmplitudeRate;
        keys = new ArrayList<>(rMap.keySet());
        keys.sort(Comparator.comparing(this::toTime));
        Collections.reverse(keys);
        firstKey = keys.get(0);

    }


    public Result calc() {
        Result ans = new Result();
        ans.setLast(LastInfo.builder().val(rMap.get(firstKey)).time(firstKey).build());
        ans.setMacro(genTrendInfo(Dimension.MACRO, macroAmplitudeFilter));
        ans.setMicro(genTrendInfo(Dimension.MICRO, null));
        return ans;

    }


    private ZonedDateTime toTime(String iso8601) {
        return ZonedDateTime.parse(iso8601);
    }


    private MinMaxInfo listMinMax(Dimension micro, Function<MinMaxInfo, MinMaxInfo> filter) {
        var ans = new MinMaxInfo();
        for (int i = 0; i < keys.size(); i++) {
            PointType mmr = findMinOrMax(i, micro);
            Point info = genMinMax(i);
            if (mmr == PointType.MINED) {
                ans.min.add(info);
                ans.all.add(info);
            } else if (mmr == PointType.MAXED) {
                ans.max.add(info);
                ans.all.add(info);
            }
        }
        if (filter != null) ans = filter.apply(ans);
        return ans;
    }

    private Point getNearInfo(List<Point> list) {
        double nt = Integer.MAX_VALUE;
        Point ans = null;
        for (var e : list) {
            var key = e.key;
            var tr = subtractKey(firstKey, key);
            if (tr < nt) {
                nt = tr;
                ans = e;
            }
        }
        return ans;

    }

    private Point genMinMax(int i) {
        String key = keys.get(i);
        return Point.builder().key(key).idx(i).val(rMap.get(key)).dtime(subtractKey(firstKey, key)).build();
    }

    private Map<Double, Point> mapInfos(List<Point> listi) {
        Map<Double, Point> ans = new HashMap<>();
        for (var e : listi) {
            ans.put(e.dtime, e);
        }
        return ans;
    }

    private double getVal(String key) {
        return rMap.get(key) - baseVal;
    }

    private boolean isBoundary(String curKey, int idx, Dimension micro, boolean forward) {
        if (idx < 0) return true;
        if (idx >= keys.size()) return true;
        String nk = keys.get(idx);
        double timeSize = Math.abs(subtractKey(curKey, nk));
        if (micro == Dimension.MICRO && timeSize > peakMaxWaitSeconds) return true;
        if (micro == Dimension.MICRO) return false;
        int nidx = forward ? idx - 1 : idx + 1;
        if (nidx < 0) return true;
        if (nidx >= keys.size()) return true;
        double cv = getVal(keys.get(idx));
        double nv = getVal(keys.get(nidx));
        return nv * cv < 0;
    }

    private PointType findMinOrMax(int idx, Dimension micro) {
        int curIdx = idx;
        String curKey = keys.get(idx);
        double curv = getVal(curKey);
        boolean maxed = true, mined = true;
        boolean forward = true;
        while (maxed || mined) {
            if (forward) idx--;
            else idx++;

            if (isBoundary(curKey, idx, micro, forward)) {
                if (forward) {
                    idx = curIdx;
                    forward = false;
                    continue;
                }

                break;
            }
            double nv = getVal(keys.get(idx));
            if (maxed) {
                if (nv > curv) maxed = false;
            }
            if (mined) {
                if (nv < curv) mined = false;
            }
        }
        if (maxed && mined) return PointType.NONE;
        if (maxed && (micro == Dimension.MICRO || curv > 0)) return PointType.MAXED;
        if (mined && (micro == Dimension.MICRO || curv < 0)) return PointType.MINED;
        return PointType.NONE;
    }

    private double subtractKey(String k1, String k2) {
        var k1t = ZonedDateTime.parse(k1);
        var k2t = ZonedDateTime.parse(k2);
        return ChronoUnit.MILLIS.between(k1t, k2t) / 1000;
    }

    private TrendInfo genTrendInfo(Dimension micro, Function<MinMaxInfo, MinMaxInfo> filter) {
        MinMaxInfo minMaxInfo = listMinMax(micro, filter);
        Point nearMax = getNearInfo(minMaxInfo.max);
        Point nearMin = getNearInfo(minMaxInfo.min);
        double maxNearTime = nearMax != null ? subtractKey(firstKey, nearMax.key) : Integer.MAX_VALUE;
        double minNearTime = nearMin != null ? subtractKey(firstKey, nearMin.key) : Integer.MAX_VALUE;
        System.out.println(nearMax);
        System.out.println(nearMin);
        Direction state = calcState(maxNearTime, minNearTime);

        System.out.println(state);
        return TrendInfo.builder()
                .state(state)
                .maxList(mapInfos(minMaxInfo.max))
                .minList(mapInfos(minMaxInfo.min))
                .allList(mapInfos(minMaxInfo.all))
                .maxNearTime(maxNearTime)
                .minNearTime(minNearTime)
                .nearMax(nearMax)
                .nearMin(nearMin)
                .nearPeak(getOtherNearPeak(state, nearMax, nearMin)).build();

    }

    private Direction calcState(double maxNearTime, double minNearTime) {
        if (maxNearTime < minNearTime) {
            return maxNearTime > peakMaxWaitSeconds ? Direction.FALL : Direction.RISE;
        } else {
            return minNearTime > peakMaxWaitSeconds ? Direction.RISE : Direction.FALL;
        }
    }

    private Point getOtherNearPeak(Direction state, Point nearMax, Point nearMin) {
        if (nearMax != null && nearMax.idx == 0) return nearMin;
        if (nearMin != null && nearMin.idx == 0) return nearMax;
        return state == Direction.FALL ? nearMin : nearMax;
    }



    public class MacroAmplitudeFilter implements Function<MinMaxInfo, MinMaxInfo> {

        @Override
        public MinMaxInfo apply(MinMaxInfo iArrays) {
            int sidx = 0;
            while (sidx < iArrays.all.size()) {
                var list = iArrays.all;
                var rmList = listAmplitudeSmall(sidx, list);
                for (var rmv : rmList) {
                    removeByIdx(iArrays.all, rmv.idx);
                    removeByIdx(iArrays.min, rmv.idx);
                    removeByIdx(iArrays.max, rmv.idx);
                }
                sidx++;
            }
            return iArrays;
        }

        private void removeByIdx(List<Point> list, int idx) {
            var po = list.stream().filter(p -> p.idx == idx).findFirst();
            if (po.isEmpty()) return;
            list.remove(po.get());
        }

        private Point checkOveAmplitudeRate(List<Point> list, int fidx, double curV) {
            double tv = Math.abs(list.get(fidx).val);
            double r = tv / curV;
            if (r > macroAmplitudeRate) {
                return null;
            }
            return list.get(fidx);
        }

        private Point getRmLater(boolean later, int idx, int pidx, double curV, List<Point> list) {
            if (!later) return null;
            int fidx = idx + pidx;
            if (fidx >= list.size()) {
                return null;
            }
            return checkOveAmplitudeRate(list, fidx, curV);

        }

        private Point getRmForward(boolean forward, int idx, int pidx, double curV, List<Point> list) {
            if (!forward) return null;
            int fidx = idx - pidx;
            if (fidx < 0) {
                return null;
            }
            return checkOveAmplitudeRate(list, fidx, curV);
        }

        private List<Point> listAmplitudeSmall(int idx, List<Point> list) {
            List<Point> ans = new ArrayList<>();
            double curV = Math.abs(list.get(idx).val);
            boolean forward = true, later = true;

            for (int i = 1; i < list.size(); i++) {
                Point fr = getRmForward(forward, idx, i, curV, list);
                if (fr == null) forward = false;
                Point lr = getRmLater(later, idx, i, curV, list);
                if (lr == null) later = false;
                if (fr != null) ans.add(fr);
                if (lr != null) ans.add(lr);
            }

            return ans;


        }

    }


}
