package net.bzk.infrastructure.tscurve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

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
        return ChronoUnit.MILLIS.between(k2t,k1t ) / 1000;
    }


    public static  ZonedDateTime toTime(String iso8601) {
        return ZonedDateTime.parse(iso8601);
    }


}