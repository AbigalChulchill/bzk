package net.bzk.infrastructure.tscurve.peak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bzk.infrastructure.tscurve.TsCurve;
import net.bzk.infrastructure.tscurve.TsHowBig;
import net.bzk.infrastructure.tscurve.dto.Point;

import java.util.Map;

public class TsBiggerFinder extends TsCurve {

    private final TsHowBig howBig;

    public TsBiggerFinder(Map<String, Double> rMap) {
        super(rMap);
        this.howBig = new TsHowBig(rMap);
    }

    public Result calc(boolean bigger,double shelfLife, double persistTime) {
        return findTargetPoint(bigger,shelfLife, persistTime);
    }



    private Result findTargetPoint(boolean bigger,double shelfLife, double persistTime) {
        for (int i = 0; i < keys.size(); i++) {
            var p = genPoint(i);
            if (p.getDtime() >= shelfLife) {// 在搜尋區間沒有找到任何的高 peaker
                return Result.builder()
                        .state(FilterState.OVER_SHELFLIFE)
                        .build();
            }
            var b = howBig.calc(TsHowBig.Dto.builder()
                    .targetKey(p.getKey())
                    .bigger(bigger)
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




    public static enum FilterState {
        FINDED, OVER_SHELFLIFE, NO_ANY_PERSIST_TIME
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PointPeakInfo {
        private Point point;
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
