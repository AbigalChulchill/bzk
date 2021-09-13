package net.bzk.infrastructure.tscurve;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.bzk.infrastructure.JsonUtils;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public abstract class TsPeakDimension {

    private Dimension dimension;
    @JsonIgnore
    private TsPeakFinder finder;

    public List<String> getKeys(){
        return finder.getKeys();
    }

    public abstract double getValByKey(String key);

    public abstract boolean isBoundary(String fromKey, int nowIdx, String nowKey ,double fromNowTime, boolean forward);

    public enum Dimension {
        MACRO, MICRO;

        public Class<? extends TsPeakDimension> getMapClass() {
            switch (this) {
                case MACRO:
                    return MacroDimension.class;
                case MICRO:
                    return MicroDimension.class;
            }
            throw new NotImplementedException(this + "not support");
        }

        public TsPeakDimension gen(Map<String, Object> m) {
            var c = getMapClass();
            return JsonUtils.toByJson(m, c);
        }

    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MicroDimension extends TsPeakDimension {

        private double peakMaxWaitSeconds;

        @Override
        public double getValByKey(String key) {
            return getFinder().getRMap().get(key);
        }

        @Override
        public boolean isBoundary(String fromKey, int nowIdx, String nowKey, double fromNowTime, boolean forward) {
             return fromNowTime > peakMaxWaitSeconds;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MacroDimension extends TsPeakDimension {
        private double baseVal;


        @Override
        public double getValByKey(String key) {
            return getFinder().getRMap().get(key) - baseVal;
        }

        @Override
        public boolean isBoundary(String fromKey, int nowIdx, String nowKey, double fromNowTime, boolean forward) {
            int nidx = forward ? nowIdx - 1 : nowIdx + 1;
            if (nidx < 0) return true;
            if (nidx >= getKeys().size()) return true;
            double cv = getValByKey(getKeys().get(nowIdx));
            double nv = getValByKey(getKeys().get(nidx));
            return nv * cv < 0;
        }
    }

}


