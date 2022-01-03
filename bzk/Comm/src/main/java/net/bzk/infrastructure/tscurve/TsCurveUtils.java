package net.bzk.infrastructure.tscurve;

import net.bzk.infrastructure.ex.BzkRuntimeException;
import net.bzk.infrastructure.tscurve.dto.Point;
import net.bzk.infrastructure.tscurve.peak.TsPeakFinder;
import org.apache.commons.lang3.StringUtils;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class TsCurveUtils {

    public enum Direction {
        RISE, FALL
    }

    public static double subtractKeySeconds(String k1, String k2) {
        var k1t = ZonedDateTime.parse(k1);
        var k2t = ZonedDateTime.parse(k2);
        return ChronoUnit.MILLIS.between(k2t, k1t) / 1000;
    }

    public static double subtractKeySecondsToNow(String k2) {
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

    public static List<Point> toPoints(Map<String, Double> rMap) {
        var keys = TsCurveUtils.sortIso8601(rMap.keySet());
        var firstKey = keys.get(0);
        List<Point> ans = new ArrayList<>();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            ans.add(Point.builder().key(key).idx(i).val(rMap.get(key)).dtime(TsCurveUtils.subtractKeySeconds(firstKey, key)).build());
        }
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

    public static List<Point> slicePoints(List<Point> ps, String key) {
        List<Point> ans = new ArrayList<>();
        for (Point p : ps) {
            ans.add(p);
            if (StringUtils.equals(p.getKey(), key)) {
                return ans;
            }
        }
        return new ArrayList<>();
    }

    public static double avgByPoints(List<Point> points) {
        return points.stream()
                .mapToDouble(p -> p.getVal())
                .average()
                .orElse(Double.NaN);
    }


}
