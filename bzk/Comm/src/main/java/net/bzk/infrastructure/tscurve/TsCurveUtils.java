package net.bzk.infrastructure.tscurve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bzk.infrastructure.tscurve.peak.TsPeakFinder;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
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

    public static double subtractKeySecondsToNow(String k2){
        var k1t = ZonedDateTime.now();
        var k2t = ZonedDateTime.parse(k2);
        return ChronoUnit.MILLIS.between(k2t, k1t) / 1000;
    }

    public static Comparator<String> ASC_TIME_ISO = (a, b) -> {
        ZonedDateTime ta = toTime(a);
        ZonedDateTime tb = toTime(b);
        return ta.compareTo(tb);
    };

    public static ZonedDateTime toTime(String iso8601) {
        return ZonedDateTime.parse(iso8601);
    }

    public static List<String> sortIso8601(Set<String> iso8601s) {
        List<String> keys = new ArrayList<>(iso8601s);
        keys.sort(Comparator.comparing(TsCurveUtils::toTime));
        Collections.reverse(keys);
        return keys;
    }

    public static List<Double> sortValues(Map<String, Double> rMap) {
        var keys = sortIso8601(rMap.keySet());
        List<Double> ans = keys.stream().map(k -> rMap.get(k)).collect(Collectors.toList());
        return ans;
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
