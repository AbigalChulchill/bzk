package net.bzk.infrastructure.tscurve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bzk.infrastructure.ex.BzkRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Map;

public class TsHowBig extends TsCurveFunc.TsCurve {


    public TsHowBig(Map<String, Double> rMap) {
        super(rMap);
    }

    public Result calc(Dto dto) {
        boolean started = false;
        double targetVal = Integer.MIN_VALUE;
        int count = 0;
        String lastKey = null;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (!started) {
                if (StringUtils.equals(key, dto.targetKey)) {
                    started = true;
                    targetVal = getAdjustVal(dto, i);
                }
                continue;
            }
            double curV = getAdjustVal(dto, i);
            count++;
            lastKey = key;
            if (curV > targetVal) {
                break;
            }

        }
        if (!started) throw new BzkRuntimeException("not find key in list : " + dto.targetKey);
        return Result.builder()
                .time(TsCurveUtils.subtractKeySeconds(dto.targetKey, lastKey))
                .endKey(lastKey)
                .count(count)
                .targetKey(dto.targetKey)
                .bigger(dto.bigger)
                .build();
    }

    private double getAdjustVal(Dto dto, int i) {
        double curV = getV(i);
        return curV * (dto.bigger ? 1 : -1);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dto {
        private String targetKey;
        private boolean bigger;
    }

    public static class DtoList extends ArrayList<Dto> {
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private String targetKey;
        private boolean bigger;
        private double time;
        private int count;
        private String endKey;

    }


}
