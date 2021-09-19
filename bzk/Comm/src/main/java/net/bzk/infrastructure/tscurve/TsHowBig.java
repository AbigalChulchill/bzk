package net.bzk.infrastructure.tscurve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bzk.infrastructure.ex.BzkRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class TsHowBig extends TsCurveFunc.TsCurve {


    private Dto dto;

    public TsHowBig(Map<String, Double> rMap, Dto d) {
        super(rMap);
        dto = d;
    }

    public Result calc() {
        boolean started = false;
        double targetVal = Integer.MIN_VALUE;
        int count = 0;
        String lastKey = null;
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            if (!started) {
                if (StringUtils.equals(key, dto.targetKey)) {
                    started = true;
                    targetVal = getAdjustVal(i);
                }
                continue;
            }
            double curV = getAdjustVal(i);
            count++;
            lastKey = key;
            if(curV > targetVal){
                break;
            }

        }
        if(!started) throw new BzkRuntimeException("not find key in list : " + dto.targetKey);
        return Result.builder()
                .time(TsCurveUtils.subtractKeySeconds(dto.targetKey, lastKey))
                .count(count)
                .targetKey(dto.targetKey)
                .build();
    }

    private double getAdjustVal(int i){
        double curV = getV(i);
        return curV * (dto.bigger ? 1 : -1);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dto {
        private String targetKey;
        private boolean bigger ;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private String targetKey;
        private double time;
        private int count;

    }


}
