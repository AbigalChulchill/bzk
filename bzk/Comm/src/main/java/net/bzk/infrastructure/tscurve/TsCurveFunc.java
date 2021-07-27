package net.bzk.infrastructure.tscurve;

import org.springframework.stereotype.Service;

import java.util.*;

public class TsCurveFunc {
    private static final TsCurveFunc instance = new TsCurveFunc();

    private TsCurveFunc() {
    }

    public PeakFinder.Result findPeak(Map<String, Double> rm, double baseVal, double peakMaxWaitSeconds, double macroAmplitudeRate) {
        PeakFinder pf = new PeakFinder(rm, baseVal, peakMaxWaitSeconds, macroAmplitudeRate);
        return pf.calc();
    }

    public TsMinMax.MinMaxPoint minmax(Map<String, Double> rm) {
        TsMinMax f = new TsMinMax(rm);
        return f.calc();
    }

    public TsContinuousDirection.Result conD(Map<String, Double> rm, TsContinuousDirection.Mode m, int thCount) {
        TsContinuousDirection tcd = new TsContinuousDirection(rm, m, thCount);
        return tcd.calc();
    }

    public static TsCurveFunc getInstance() {
        return instance;
    }

    public static class TsCurve {
        protected final Map<String, Double> rMap;
        protected final List<String> keys;
        protected final String firstKey;

        public TsCurve(Map<String, Double> rMap) {
            this.rMap = rMap;
            keys = new ArrayList<>(rMap.keySet());
            keys.sort(Comparator.comparing(TsCurveUtils::toTime));
            Collections.reverse(keys);
            firstKey = keys.get(0);
        }

        public TsCurveUtils.Point genPoint(int i) {
            String key = keys.get(i);
            return TsCurveUtils.Point.builder().key(key).idx(i).val(rMap.get(key)).dtime(TsCurveUtils.subtractKeySeconds(firstKey, key)).build();
        }

        public double getV(int idx) {
            String k = keys.get(idx);
            return rMap.get(k);
        }

    }
}
