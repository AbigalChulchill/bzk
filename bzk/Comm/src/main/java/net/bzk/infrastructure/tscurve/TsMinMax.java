package net.bzk.infrastructure.tscurve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import net.bzk.infrastructure.tscurve.dto.Point;

public class TsMinMax extends TsCurve {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MinMaxIdx {
        private int minIdx;
        private int maxIdx;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MinMaxPoint {
        private Point min;
        private Point max;
        private Point near;
        private Point far;
        private String nearKey;
    }


    public TsMinMax(Map<String, Double> rMap) {
        super(rMap);
    }

    private MinMaxIdx findMinMaxIdx() {
        double _min = Integer.MAX_VALUE;
        double _max = Integer.MIN_VALUE;
        int _minIdx = -1;
        int _maxIdx = -1;

        for (int i = 0; i < keys.size(); i++) {
            double curV = getV(i);
            if (curV > _max) {
                _max = curV;
                _maxIdx = i;
            }
            if (curV < _min) {
                _min = curV;
                _minIdx = i;
            }
        }
        return MinMaxIdx.builder().minIdx(_minIdx).maxIdx(_maxIdx).build();
    }




    public MinMaxPoint calc() {
        var mm = findMinMaxIdx();
        String nearKey = null;
        Point near,far;
        var min = genPoint(mm.minIdx);
        var max = genPoint(mm.maxIdx);
        if(min.getDtime()<max.getDtime()){
            near = min;
            far = max;
            nearKey = "min";
        }else{
            near = max;
            far = min;
            nearKey = "max";
        }
        return MinMaxPoint.builder()
                .min(min)
                .max(max)
                .nearKey(nearKey)
                .near(near)
                .far(far)
                .build();
    }

}
