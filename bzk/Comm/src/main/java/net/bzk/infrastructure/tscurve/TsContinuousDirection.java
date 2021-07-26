package net.bzk.infrastructure.tscurve;

import java.util.List;
import java.util.Map;

import lombok.Data;
import net.bzk.infrastructure.tscurve.TsCurveUtils.Point;
import import net.bzk.infrastructure.tscurve.TsCurveUtils.Direction;

public class TsContinuousDirection extends TsCurveFunc.TsCurve {

    @Data
    public class Range{
        private Point start;
        private Point end;
        private int count;

    }

    @Data
    public class ContinuousInfo{
        private Range range;
        private Direction direction;
        private float stepRate;

    }

    public TsContinuousDirection(Map<String, Double> rMap) {
        super(rMap);
    }

    private int appendContinuousInfo(List<ContinuousInfo> list, int idx){
        if(idx >= keys.size()-1 ) return -1;
        int startIdx = idx , endIdx = idx +1;
        double curDV = getV(startIdx) - getV(endIdx);
        while ()

    }




}
