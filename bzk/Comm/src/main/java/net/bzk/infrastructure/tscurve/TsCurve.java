package net.bzk.infrastructure.tscurve;

import lombok.Getter;

import java.util.List;
import java.util.Map;

public class TsCurve {
    @Getter
    protected final Map<String, Double> rMap;
    @Getter
    protected final List<String> keys;
    @Getter
    protected final String firstKey;

    public TsCurve(Map<String, Double> rMap) {
        this.rMap = rMap;
        keys = TsCurveUtils.sortIso8601(rMap.keySet());
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
