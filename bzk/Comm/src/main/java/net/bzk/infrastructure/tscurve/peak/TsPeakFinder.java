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

    public static class AtPointMap extends HashMap<Double, Point> {

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TrendInfo {
        private Direction state;
        private AtPointMap maxList;
        private AtPointMap minList;
        private AtPointMap allList;
        private double maxNearTime;
        private double minNearTime;
        private Point nearMax;
        private Point nearMin;
        private Point nearPeak;
        private PointType nearPeakType;
    }


    private final TsPeakLogic peakLogic;


    public TsPeakFinder(Map<String, Double> rm, TsPeakLogic tpd) {
        super(rm);
        this.peakLogic = tpd;
        this.peakLogic.setFinder(this);
    }


    public Result calc() {
        Result ans = new Result();
        ans.setLast(LastInfo.builder().val(rMap.get(firstKey)).time(firstKey).build());
        ans.setTrendInfo(genTrendInfo());
        ans= peakLogic.fixResult(ans) ;
        return ans;

    }


    private MinMaxInfo listMinMax() {
        var ans = new MinMaxInfo();
        for (int i = 0; i < keys.size(); i++) {
            PointType mmr = peakLogic.findMinOrMax(i);
            Point info = genPoint(i);
            if (mmr == PointType.MINED) {
                ans.min.add(info);
                ans.all.add(info);
            } else if (mmr == PointType.MAXED) {
                ans.max.add(info);
                ans.all.add(info);
            }
        }
        ans = peakLogic.filterAmplitude(ans);
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

    private AtPointMap mapInfos(List<Point> listi) {
        AtPointMap ans = new AtPointMap();
        for (var e : listi) {
            ans.put(e.getDtime(), e);
        }
        return ans;
    }


    private TrendInfo genTrendInfo() {
        MinMaxInfo minMaxInfo = listMinMax();
        Point nearMax = getNearInfo(minMaxInfo.max);
        Point nearMin = getNearInfo(minMaxInfo.min);
        double maxNearTime = nearMax != null ? TsCurveUtils.subtractKeySeconds(firstKey, nearMax.getKey()) : Integer.MAX_VALUE;
        double minNearTime = nearMin != null ? TsCurveUtils.subtractKeySeconds(firstKey, nearMin.getKey()) : Integer.MAX_VALUE;
        System.out.println(nearMax);
        System.out.println(nearMin);
        Direction state = peakLogic.calcState(maxNearTime, minNearTime);
        var peakType = getOtherNearPeak(state, nearMax, nearMin);
        Point nearPoint = peakType == PointType.MAXED ? nearMax : nearMin;

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
                .nearPeak(nearPoint)
                .nearPeakType(peakType).build();

    }

    private PointType getOtherNearPeak(Direction state, Point nearMax, Point nearMin) {
        if (nearMax != null && nearMax.getIdx() == 0) return PointType.MINED;
        if (nearMin != null && nearMin.getIdx() == 0) return PointType.MAXED;
        return state == Direction.FALL ? PointType.MAXED : PointType.MINED;
    }

    @Override
    public Point genPoint(int i) {
        return peakLogic.genPoint(i);
    }
}
