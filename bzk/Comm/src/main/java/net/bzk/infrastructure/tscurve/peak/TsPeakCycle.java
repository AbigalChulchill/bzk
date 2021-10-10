package net.bzk.infrastructure.tscurve.peak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class TsPeakCycle {
    private TsPeakFinder.TrendInfo trendInfo;

    public TsPeakCycle(TsPeakFinder.TrendInfo trendInfo) {
        this.trendInfo = trendInfo;
    }

    public Result calc() {
        return Result.builder()
                .avgRadius(calcAvgRadius())
                .build();

    }

    private double calcAvgRadius() {
        var all = trendInfo.getAllList();
        List<Double> timeKeys = new ArrayList<>(all.keySet());
        timeKeys.sort((a, b) -> Double.compare(a, b));
        double sum = 0;
        double count = 0;
        double lastKey = 0;
        for (Double t : timeKeys) {
            if (t == 0) continue;
            double dt = t - lastKey;
            lastKey = t;
            sum += dt;
            count++;
        }
        return sum / count;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private double avgRadius;
    }
}
