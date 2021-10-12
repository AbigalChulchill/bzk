package net.bzk.infrastructure.tscurve;

import lombok.Getter;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.tscurve.peak.Dimension;
import net.bzk.infrastructure.tscurve.peak.TsPeakCycle;
import net.bzk.infrastructure.tscurve.peak.TsPeakFinder;
import net.bzk.infrastructure.tscurve.peak.TsTrendPeakFilter;

import java.util.*;

public class TsCurveFunc {
    private static final TsCurveFunc instance = new TsCurveFunc();

    private TsCurveFunc() {
    }

    public TsPeakFinder.Result findPeak(Map<String, Double> rm, Map<String, Object> dimensionMap) {
        Dimension d = Dimension.valueOf(dimensionMap.get("dimension").toString());
        var dto = d.genDto(dimensionMap);
        var logic = d.genLogic();
        logic.setDto(dto);
        TsPeakFinder pf = new TsPeakFinder(rm, logic);
        return pf.calc();
    }

    public TsMinMax.MinMaxPoint minmax(Map<String, Double> rm) {
        TsMinMax f = new TsMinMax(rm);
        return f.calc();
    }

    public TsContinuousDirection.Result conD(Map<String, Double> rm, String m, int thCount) {
        TsContinuousDirection.Mode mode = TsContinuousDirection.Mode.valueOf(m);
        TsContinuousDirection tcd = new TsContinuousDirection(rm, mode, thCount);
        return tcd.calc();
    }

    public TsHowBig.Result findBigger(Map<String, Double> rm, String dtoJson) {
        TsHowBig.Dto dto = JsonUtils.loadByJson(dtoJson, TsHowBig.Dto.class);
        TsHowBig tb = new TsHowBig(rm);
        return tb.calc(dto);
    }

    public TsTrendPeakFilter.Result filterPeak(Map<String, Double> rMap, String infoJson, double shelfLife, double persistTime) {
        TsPeakFinder.TrendInfo info = JsonUtils.loadByJson(infoJson, TsPeakFinder.TrendInfo.class);
        TsTrendPeakFilter tf = new TsTrendPeakFilter(rMap, info);
        return tf.calc(shelfLife, persistTime);
    }


    public TsPeakCycle.Result calcCycle(String tendInfo) {
        TsPeakFinder.TrendInfo ti = JsonUtils.loadByJson(tendInfo, TsPeakFinder.TrendInfo.class);
        TsPeakCycle tc = new TsPeakCycle(ti);
        return tc.calc();
    }

    public static TsCurveFunc getInstance() {
        return instance;
    }

    public static class TsCurve {
        @Getter
        protected final Map<String, Double> rMap;
        @Getter
        protected final List<String> keys;
        @Getter
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
