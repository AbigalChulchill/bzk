package net.bzk.infrastructure.tscurve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.function.Function;

import net.bzk.infrastructure.tscurve.TsCurveUtils.Point;
import net.bzk.infrastructure.tscurve.TsCurveUtils.Direction;


public class TsPeakFinder extends TsCurveFunc.TsCurve {

    public enum PointType {
        MINED, MAXED, NONE
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
    public static class TrendInfo {
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


//    private final double baseVal;
//    private final double peakMaxWaitSeconds;
//    private final double macroAmplitudeRate;
    private  final  TsPeakDimension dimension;
    private final MacroAmplitudeFilter macroAmplitudeFilter = new MacroAmplitudeFilter();


    public TsPeakFinder(Map<String, Double> rm, TsPeakDimension tpd) {
        super(rm);
        this.dimension = tpd;
//        this.baseVal = baseVal;
//        this.peakMaxWaitSeconds = peakMaxWaitSeconds;
//        this.macroAmplitudeRate = macroAmplitudeRate;

    }


    public Result calc() {
        Result ans = new Result();
        ans.setLast(LastInfo.builder().val(rMap.get(firstKey)).time(firstKey).build());
        ans.setMacro(genTrendInfo(TsPeakDimension.Dimension.MACRO, macroAmplitudeFilter));
        ans.setMicro(genTrendInfo(TsPeakDimension.Dimension.MICRO, null));
        return ans;

    }


    private MinMaxInfo listMinMax(TsPeakDimension.Dimension micro, Function<MinMaxInfo, MinMaxInfo> filter) {
        var ans = new MinMaxInfo();
        for (int i = 0; i < keys.size(); i++) {
            PointType mmr = findMinOrMax(i, micro);
            Point info = genPoint(i);
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
            var key = e.getKey();
            var tr = TsCurveUtils.subtractKeySeconds(firstKey, key);
            if (tr < nt) {
                nt = tr;
                ans = e;
            }
        }
        return ans;

    }

    private Map<Double, Point> mapInfos(List<Point> listi) {
        Map<Double, Point> ans = new HashMap<>();
        for (var e : listi) {
            ans.put(e.getDtime(), e);
        }
        return ans;
    }

    public double getValByKey(String key) {
        return dimension.getValByKey(key);
    }

    private boolean isBoundary(String fromKey, int nowIdx, TsPeakDimension.Dimension micro, boolean forward) {
        if (nowIdx < 0) return true;
        if (nowIdx >= keys.size()) return true;
        String nowKey = keys.get(nowIdx);
        double fromNowTime = Math.abs(TsCurveUtils.subtractKeySeconds(fromKey, nowKey));
        return dimension.isBoundary(fromKey,nowIdx,nowKey,fromNowTime,forward);
    }

    private PointType findMinOrMax(int idx, TsPeakDimension.Dimension micro) {
        int curIdx = idx;
        String curKey = keys.get(idx);
        double curv = getValByKey(curKey);
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
            double nv = getValByKey(keys.get(idx));
            if (maxed) {
                if (nv > curv) maxed = false;
            }
            if (mined) {
                if (nv < curv) mined = false;
            }
        }
        if (maxed && mined) return PointType.NONE;
        if (maxed && (micro == TsPeakDimension.Dimension.MICRO || curv > 0)) return PointType.MAXED;
        if (mined && (micro == TsPeakDimension.Dimension.MICRO || curv < 0)) return PointType.MINED;
        return PointType.NONE;
    }


    private TrendInfo genTrendInfo(TsPeakDimension.Dimension micro, Function<MinMaxInfo, MinMaxInfo> filter) {
        MinMaxInfo minMaxInfo = listMinMax(micro, filter);
        Point nearMax = getNearInfo(minMaxInfo.max);
        Point nearMin = getNearInfo(minMaxInfo.min);
        double maxNearTime = nearMax != null ? TsCurveUtils.subtractKeySeconds(firstKey, nearMax.getKey()) : Integer.MAX_VALUE;
        double minNearTime = nearMin != null ? TsCurveUtils.subtractKeySeconds(firstKey, nearMin.getKey()) : Integer.MAX_VALUE;
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
        if (nearMax != null && nearMax.getIdx() == 0) return nearMin;
        if (nearMin != null && nearMin.getIdx() == 0) return nearMax;
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
                    removeByIdx(iArrays.all, rmv.getIdx());
                    removeByIdx(iArrays.min, rmv.getIdx());
                    removeByIdx(iArrays.max, rmv.getIdx());
                }
                sidx++;
            }
            return iArrays;
        }

        private void removeByIdx(List<Point> list, int idx) {
            var po = list.stream().filter(p -> p.getIdx() == idx).findFirst();
            if (po.isEmpty()) return;
            list.remove(po.get());
        }

        private Point checkOveAmplitudeRate(List<Point> list, int fidx, double curV) {
            double tv = Math.abs(list.get(fidx).getVal());
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
            double curV = Math.abs(list.get(idx).getVal());
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
