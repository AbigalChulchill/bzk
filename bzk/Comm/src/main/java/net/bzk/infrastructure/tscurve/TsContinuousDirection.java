package net.bzk.infrastructure.tscurve;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bzk.infrastructure.tscurve.TsCurveUtils.Point;
import
import net.bzk.infrastructure.tscurve.TsCurveUtils.Direction;

public class TsContinuousDirection extends TsCurveFunc.TsCurve {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class Range {
        private Point start;
        private Point end;
        private int count;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class ContinuousInfo {
        private Range range;
        private Direction direction;
        private float stepRate;
        private double dv;

    }

    private final int thCount;

    public TsContinuousDirection(Map<String, Double> rMap, int thCount) {
        super(rMap);
        this.thCount = thCount;
    }

    private int appendContinuousInfo(List<ContinuousInfo> list, int idx) {
        if (idx >= keys.size() - 2) return -1;
        int initStartIdx = idx;
        boolean curG0 = true;
        boolean nextG0 = true;
        int count = 0;
        int lastEnd = -1;
        while (curG0 == nextG0) {
            double curDV = getV(idx) - getV(idx + 1);
            curG0 = isGreaterZero(curDV);
            lastEnd = idx +2;
            double nextDv = getV(idx + 1) - getV(lastEnd);
            nextG0 = isGreaterZero(nextDv);
            idx++;
            count++;
        }
        if (count >= thCount) list.add(genContinuousInfo(initStartIdx, lastEnd));
        return lastEnd;
    }

    private ContinuousInfo genContinuousInfo(int start, int end) {
        double startV = getV(start), endV = getV(end);
        Direction d = endV > startV ? Direction.FALL : Direction.RISE;
        return ContinuousInfo.builder()
                .direction(d)
                .dv(Math.abs(endV - startV))
                .range(
                        Range.builder()
                                .start(genPoint(start))
                                .end(genPoint(end))
                                .count(end - start)
                                .build()
                )
                .build();
    }

    private boolean isGreaterZero(double v) {
        return v >= 0;
    }


}
