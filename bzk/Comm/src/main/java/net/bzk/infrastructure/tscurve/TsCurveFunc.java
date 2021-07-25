package net.bzk.infrastructure.tscurve;

import org.springframework.stereotype.Service;

import java.util.Map;

public class TsCurveFunc {
    private static final TsCurveFunc instance = new TsCurveFunc();

    private TsCurveFunc() {
    }

    public PeakFinder.Result findPeak(Map<String, Double> rm, double baseVal, double peakMaxWaitSeconds, double macroAmplitudeRate) {
        PeakFinder pf = new PeakFinder(rm, baseVal, peakMaxWaitSeconds, macroAmplitudeRate);
        return pf.calc();
    }

    public static TsCurveFunc getInstance() {
        return instance;
    }
}
