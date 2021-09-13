package net.bzk.infrastructure.tscurve;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import net.bzk.infrastructure.JsonUtils;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public abstract class TsPeakDimension {

    private double peakMaxWaitSeconds;
    private Dimension dimension;
    @JsonIgnore
    private TsPeakFinder finder;

    public List<String> getKeys() {
        return finder.getKeys();
    }

    public TsPeakFinder.PointType checkMinMax(boolean maxed, boolean mined, double fromVal) {
        if (maxed && mined) return TsPeakFinder.PointType.NONE;
        return checkMinMaxEx(maxed, mined, fromVal);
    }

    protected void removeByIdx(List<TsCurveUtils.Point> list, int idx) {
        var po = list.stream().filter(p -> p.getIdx() == idx).findFirst();
        if (po.isEmpty()) return;
        list.remove(po.get());
    }

    public TsCurveUtils.Direction calcState(double maxNearTime, double minNearTime) {
        if (maxNearTime < minNearTime) {
            return maxNearTime > peakMaxWaitSeconds ? TsCurveUtils.Direction.FALL : TsCurveUtils.Direction.RISE;
        } else {
            return minNearTime > peakMaxWaitSeconds ? TsCurveUtils.Direction.RISE : TsCurveUtils.Direction.FALL;
        }
    }

    protected abstract TsPeakFinder.PointType checkMinMaxEx(boolean maxed, boolean mined, double fromVal);

    public abstract TsPeakFinder.MinMaxInfo filterAmplitude(TsPeakFinder.MinMaxInfo iArrays);

    public abstract double getValByKey(String key);

    public abstract boolean isBoundary(String fromKey, int nowIdx, String nowKey, double fromNowTime, boolean forward);

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



        @Override
        protected TsPeakFinder.PointType checkMinMaxEx(boolean maxed, boolean mined, double fromVal) {
            if (maxed) return TsPeakFinder.PointType.MAXED;
            if (mined) return TsPeakFinder.PointType.MINED;
            return TsPeakFinder.PointType.NONE;
        }

        @Override
        public TsPeakFinder.MinMaxInfo filterAmplitude(TsPeakFinder.MinMaxInfo iArrays) {
            return iArrays;
        }

        @Override
        public double getValByKey(String key) {
            return getFinder().getRMap().get(key);
        }

        @Override
        public boolean isBoundary(String fromKey, int nowIdx, String nowKey, double fromNowTime, boolean forward) {
            return fromNowTime > getPeakMaxWaitSeconds() ;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MacroDimension extends TsPeakDimension {
        private double baseVal;
        private double amplitudeRate;


        @Override
        protected TsPeakFinder.PointType checkMinMaxEx(boolean maxed, boolean mined, double fromVal) {
            if (maxed && fromVal > 0) return TsPeakFinder.PointType.MAXED;
            if (mined && fromVal < 0) return TsPeakFinder.PointType.MINED;
            return TsPeakFinder.PointType.NONE;
        }

        @Override
        public TsPeakFinder.MinMaxInfo filterAmplitude(TsPeakFinder.MinMaxInfo iArrays) {
            int sidx = 0;
            while (sidx < iArrays.getAll().size()) {
                var list = iArrays.getAll();
                var rmList = listAmplitudeSmall(sidx, list);
                for (var rmv : rmList) {
                    removeByIdx(iArrays.getAll(), rmv.getIdx());
                    removeByIdx(iArrays.getMin(), rmv.getIdx());
                    removeByIdx(iArrays.getMax(), rmv.getIdx());
                }
                sidx++;
            }
            return iArrays;
        }

        private List<TsCurveUtils.Point> listAmplitudeSmall(int idx, List<TsCurveUtils.Point> list) {
            List<TsCurveUtils.Point> ans = new ArrayList<>();
            double curV = Math.abs(list.get(idx).getVal());
            boolean forward = true, later = true;

            for (int i = 1; i < list.size(); i++) {
                TsCurveUtils.Point fr = getRmForward(forward, idx, i, curV, list);
                if (fr == null) forward = false;
                TsCurveUtils.Point lr = getRmLater(later, idx, i, curV, list);
                if (lr == null) later = false;
                if (fr != null) ans.add(fr);
                if (lr != null) ans.add(lr);
            }

            return ans;
        }

        private TsCurveUtils.Point getRmLater(boolean later, int idx, int pidx, double curV, List<TsCurveUtils.Point> list) {
            if (!later) return null;
            int fidx = idx + pidx;
            if (fidx >= list.size()) {
                return null;
            }
            return checkOveAmplitudeRate(list, fidx, curV);

        }

        private TsCurveUtils.Point getRmForward(boolean forward, int idx, int pidx, double curV, List<TsCurveUtils.Point> list) {
            if (!forward) return null;
            int fidx = idx - pidx;
            if (fidx < 0) {
                return null;
            }
            return checkOveAmplitudeRate(list, fidx, curV);
        }

        private TsCurveUtils.Point checkOveAmplitudeRate(List<TsCurveUtils.Point> list, int fidx, double curV) {
            double tv = Math.abs(list.get(fidx).getVal());
            double r = tv / curV;
            if (r > amplitudeRate) {
                return null;
            }
            return list.get(fidx);
        }

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


