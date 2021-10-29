package net.bzk.infrastructure.tscurve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bzk.infrastructure.tscurve.peak.TsPeakFinder;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TsCurveUtils {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Point {
        private int idx;
        private String key;
        private double val;
        private double dtime;
    }

    public enum Direction {
        RISE, FALL
    }

    public static double subtractKeySeconds(String k1, String k2) {
        var k1t = ZonedDateTime.parse(k1);
        var k2t = ZonedDateTime.parse(k2);
        return ChronoUnit.MILLIS.between(k2t, k1t) / 1000;
    }


    public static ZonedDateTime toTime(String iso8601) {
        return ZonedDateTime.parse(iso8601);
    }

    public static List<Double> sortTimeKeys(Set<Double> keys) {
        List<Double> timeKeys = new ArrayList<>(keys);
        timeKeys.sort((a, b) -> Double.compare(a, b));
        return timeKeys;
    }

    public static List<Point> sortPoints(TsPeakFinder.AtPointMap apm) {
        var sortKeys = sortTimeKeys(apm.keySet());
        return sortKeys.stream().map(k -> apm.get(k)).collect(Collectors.toList());
    }


}
