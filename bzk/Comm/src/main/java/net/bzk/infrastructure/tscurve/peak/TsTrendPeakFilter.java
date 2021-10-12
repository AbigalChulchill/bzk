package net.bzk.infrastructure.tscurve.peak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bzk.infrastructure.ex.BzkRuntimeException;
import net.bzk.infrastructure.tscurve.TsCurveUtils;
import net.bzk.infrastructure.tscurve.TsHowBig;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Map;

public class TsTrendPeakFilter {

    private final TsPeakFinder.TrendInfo info;
    private final Map<String, Double> rMap;
    private final TsHowBig howBig;

    public TsTrendPeakFilter(Map<String, Double> rMap, TsPeakFinder.TrendInfo info) {
        this.info = info;
        this.rMap = rMap;
        this.howBig = new TsHowBig(this.rMap);
    }

    public Result calc(double shelfLife, double persistTime) {
        return findTargetPoint(shelfLife, persistTime);
    }

    private Result findTargetPoint(double shelfLife, double persistTime) {
        var pMap = getTargetAtMap();
        List<Double> keys = TsCurveUtils.sortTimeKeys(pMap.keySet());
        for (Double k : keys) {
            if (k > shelfLife) {// 在搜尋區間沒有找到任何的高 peaker
                return Result.builder()
                        .state(FilterState.OVER_SHELFLIFE)
                        .build();
            }
            TsCurveUtils.Point p = pMap.get(k);
            var b = howBig.calc(TsHowBig.Dto.builder()
                    .targetKey(p.getKey())
                    .bigger(info.getState() == TsCurveUtils.Direction.FALL)
                    .build());
            if (b.getTime() > persistTime) {
                return Result.builder()
                        .state(FilterState.FINDED)
                        .peakInfo(PointPeakInfo.builder()
                                .point(p)
                                .biggerInfo(b)
                                .build())
                        .build();
            }

        }
        return Result.builder().state(FilterState.NO_ANY_PERSIST_TIME).build();
    }

    private TsPeakFinder.AtPointMap getTargetAtMap() {
        switch (info.getState()) {
            case FALL:
                return info.getMaxList();
            case RISE:
                return info.getMinList();
        }
        throw new BzkRuntimeException("info state " + info.getState());
    }


    public static enum FilterState {
        FINDED, OVER_SHELFLIFE, NO_ANY_PERSIST_TIME
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointPeakInfo {
        private TsCurveUtils.Point point;
        private TsHowBig.Result biggerInfo;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {

        private PointPeakInfo peakInfo;
        private FilterState state;

    }

}
