package net.bzk.infrastructure.tscurve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bzk.infrastructure.tscurve.TsCurveUtils.Direction;
import net.bzk.infrastructure.tscurve.TsCurveUtils.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TsContinuousDirection extends TsCurveFunc.TsCurve {

    public static enum Mode {
        UNIFORM_SLOPE
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Range {
        private Point start;
        private Point end;
        private int count;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContinuousInfo {
        private Range range;
        private Direction direction;
        private float stepRate;
        private double dv;

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private List<ContinuousInfo> continuous;
        private ContinuousInfo lastContinuous;
        private Point last;
    }

    public abstract class ModeLogic {
        protected final TsCurveFunc.TsCurve curve;

        protected ModeLogic(TsCurveFunc.TsCurve curve) {
            this.curve = curve;
        }

        abstract int calcEndIdx(int startIdx);
    }

    public class UniformSlopeLogic extends ModeLogic {

        protected UniformSlopeLogic(TsCurveFunc.TsCurve curve) {
            super(curve);
        }

        @Override
        int calcEndIdx(int idx) {

            boolean next = false;
            int lastEnd = -1;
            do {
                if (idx >= keys.size() - 3) return -1;
                double curDV = getV(idx) - getV(idx + 1);
                boolean curG0 = isGreaterZero(curDV);

                double nextDv = getV(idx + 1) - getV(idx + 2);
                boolean nextG0 = isGreaterZero(nextDv);
                if (curG0 == nextG0) {
                    lastEnd = idx + 2;
                    next = true;
                } else {
                    next = false;
                }
                idx++;
            } while (next);
            return lastEnd;
        }
    }

    private final int thCount;
    private final Mode mode;

    public TsContinuousDirection(Map<String, Double> rMap, Mode m, int thCount) {
        super(rMap);
        mode = m;
        this.thCount = thCount;
    }

    private Map<Mode, ModeLogic> _logic_cache = new ConcurrentHashMap<>();

    private ModeLogic getLogic() {
        switch (mode) {
            case UNIFORM_SLOPE:
                if (!_logic_cache.containsKey(mode)) {
                    _logic_cache.put(mode, new UniformSlopeLogic(this));
                }
                return _logic_cache.get(mode);
        }
        throw new NullPointerException("not support " + mode);
    }

    public Result calc() {
        int _idx = 0;
        List<ContinuousInfo> infos = new ArrayList<>();
        do {
            _idx = appendContinuousInfo(infos, _idx);
        } while (_idx < keys.size());
        ContinuousInfo lc = infos.size() > 0 ? infos.get(0) : null;
        return Result.builder()
                .continuous(infos)
                .lastContinuous(lc)
                .last(genPoint(0))
                .build();
    }

    private int appendContinuousInfo(List<ContinuousInfo> list, int idx) {
        int endIdx = getLogic().calcEndIdx(idx);
        if (endIdx < 0) return idx + 1;
        int count = endIdx - idx;
        if (count >= thCount) list.add(genContinuousInfo(idx, endIdx));
        return endIdx;
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
