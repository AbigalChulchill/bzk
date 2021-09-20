package net.bzk.infrastructure.tscurve.peak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bzk.infrastructure.tscurve.TsCurveFunc;
import net.bzk.infrastructure.tscurve.TsCurveUtils;
import net.bzk.infrastructure.tscurve.TsCurveUtils.Direction;
import net.bzk.infrastructure.tscurve.TsCurveUtils.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        private TrendInfo trendInfo;
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
        private PointType nearPeakType;
    }


//    private final double baseVal;
//    private final double peakMaxWaitSeconds;
//    private final double macroAmplitudeRate;
    private  final  TsPeakDimension dimension;


    public TsPeakFinder(Map<String, Double> rm, TsPeakDimension tpd) {
        super(rm);
        this.dimension = tpd;
        this.dimension.setFinder(this);
    }


    public Result calc() {
        Result ans = new Result();
        ans.setLast(LastInfo.builder().val(rMap.get(firstKey)).time(firstKey).build());
        ans.setTrendInfo(genTrendInfo());
        return ans;

    }


    private MinMaxInfo listMinMax( ) {
        var ans = new MinMaxInfo();
        for (int i = 0; i < keys.size(); i++) {
            PointType mmr = findMinOrMax(i);
            Point info = genPoint(i);
            if (mmr == PointType.MINED) {
                ans.min.add(info);
                ans.all.add(info);
            } else if (mmr == PointType.MAXED) {
                ans.max.add(info);
                ans.all.add(info);
            }
        }
        ans = dimension.filterAmplitude(ans);
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

    private boolean isBoundary(String fromKey, int nowIdx, boolean forward) {
        if (nowIdx < 0) return true;
        if (nowIdx >= keys.size()) return true;
        String nowKey = keys.get(nowIdx);
        double fromNowTime = Math.abs(TsCurveUtils.subtractKeySeconds(fromKey, nowKey));
        return dimension.isBoundary(fromKey,nowIdx,nowKey,fromNowTime,forward);
    }

    private PointType findMinOrMax(int idx) {
        int fromIdx = idx;
        String fromKey = keys.get(fromIdx);
        double fromVal = getValByKey(fromKey);
        boolean maxed = true, mined = true;
        boolean forward = true;
        while (maxed || mined) {
            if (forward) idx--;
            else idx++;

            if (isBoundary(fromKey, idx, forward)) {
                if (forward) {
                    idx = fromIdx;
                    forward = false;
                    continue;
                }

                break;
            }
            double nowVal = getValByKey(keys.get(idx));
            if (maxed) {
                if (nowVal > fromVal) maxed = false;
            }
            if (mined) {
                if (nowVal < fromVal) mined = false;
            }
        }
        return dimension.checkMinMax(maxed,mined,fromVal);
    }


    private TrendInfo genTrendInfo( ) {
        MinMaxInfo minMaxInfo = listMinMax( );
        Point nearMax = getNearInfo(minMaxInfo.max);
        Point nearMin = getNearInfo(minMaxInfo.min);
        double maxNearTime = nearMax != null ? TsCurveUtils.subtractKeySeconds(firstKey, nearMax.getKey()) : Integer.MAX_VALUE;
        double minNearTime = nearMin != null ? TsCurveUtils.subtractKeySeconds(firstKey, nearMin.getKey()) : Integer.MAX_VALUE;
        System.out.println(nearMax);
        System.out.println(nearMin);
        Direction state = dimension. calcState(maxNearTime, minNearTime);

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
                .nearPeakType(getOtherNearPeak(state, nearMax, nearMin)).build();

    }

    private PointType getOtherNearPeak(Direction state, Point nearMax, Point nearMin) {
        if (nearMax != null && nearMax.getIdx() == 0) return  PointType.MINED;
        if (nearMin != null && nearMin.getIdx() == 0) return  PointType.MAXED;
        return state == Direction.FALL ? PointType.MINED : PointType.MAXED;
    }

}
