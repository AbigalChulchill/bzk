package net.bzk.infrastructure.tscurve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import net.bzk.infrastructure.tscurve.TsCurveUtils.Point;

public class TsMinMax extends TsCurveFunc.TsCurve {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private class MinMaxIdx {
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
        return MinMaxPoint.builder().min(genPoint(mm.minIdx)).max(genPoint(mm.maxIdx)).build();
    }

}
