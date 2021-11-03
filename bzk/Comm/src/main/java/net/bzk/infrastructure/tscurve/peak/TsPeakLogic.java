package net.bzk.infrastructure.tscurve.peak;

import lombok.*;
import net.bzk.infrastructure.tscurve.TsCurveUtils;
import net.bzk.infrastructure.tscurve.TsHowBig;

import java.util.ArrayList;
import java.util.List;


public abstract class TsPeakLogic<T extends PeakLogicDto> {


    @Setter
    protected T dto;
    @Setter
    protected TsPeakFinder finder;


    public List<String> getKeys() {
        return finder.getKeys();
    }


    protected void removeByIdx(List<TsCurveUtils.Point> list, int idx) {
        var po = list.stream().filter(p -> p.getIdx() == idx).findFirst();
        if (po.isEmpty()) return;
        list.remove(po.get());
    }

    public abstract TsCurveUtils.Direction calcState(double maxNearTime, double minNearTime);


    public abstract TsPeakFinder.MinMaxInfo filterAmplitude(TsPeakFinder.MinMaxInfo iArrays);

    public abstract double getValByKey(String key);

    public abstract TsPeakFinder.PointType findMinOrMax(int idx);


    @Data
    @EqualsAndHashCode(callSuper = false)
    public static abstract class WaitLogic<E extends PeakLogicDto.WaitPeakLogicDto> extends TsPeakLogic<E> {


        private boolean isBoundary(String fromKey, int nowIdx, boolean forward) {
            if (nowIdx < 0) return true;
            if (nowIdx >= getKeys().size()) return true;
            String nowKey = getKeys().get(nowIdx);
            double fromNowTime = Math.abs(TsCurveUtils.subtractKeySeconds(fromKey, nowKey));
            return isBoundary(fromKey, nowIdx, nowKey, fromNowTime, forward);
        }

        public TsPeakFinder.PointType findMinOrMax(int idx) {
            int fromIdx = idx;
            String fromKey = getKeys().get(fromIdx);
            double fromVal = getValByKey(fromKey);
            boolean maxed = true, mined = true;
            boolean forward = true;
            while (maxed || mined) {
                if (forward) idx--;
                else idx++;

                if (isBoundary(fromKey, idx, forward)) {
                    if (forward) {
                        idx = fromIdx;
                        forward = false;
                        continue;
                    }

                    break;
                }
                double nowVal = getValByKey(getKeys().get(idx));
                if (maxed) {
                    if (nowVal > fromVal) maxed = false;
                }
                if (mined) {
                    if (nowVal < fromVal) mined = false;
                }
            }
            return checkMinMax(maxed, mined, fromVal);
        }

        public TsPeakFinder.PointType checkMinMax(boolean maxed, boolean mined, double fromVal) {
            if (maxed && mined) return TsPeakFinder.PointType.NONE;
            return checkMinMaxEx(maxed, mined, fromVal);
        }

        public TsCurveUtils.Direction calcState(double maxNearTime, double minNearTime) {
            if (maxNearTime < minNearTime) {
                return maxNearTime > dto.peakMaxWaitSeconds ? TsCurveUtils.Direction.FALL : TsCurveUtils.Direction.RISE;
            } else {
                return minNearTime > dto.peakMaxWaitSeconds ? TsCurveUtils.Direction.RISE : TsCurveUtils.Direction.FALL;
            }
        }

        protected abstract TsPeakFinder.PointType checkMinMaxEx(boolean maxed, boolean mined, double fromVal);

        public abstract boolean isBoundary(String fromKey, int nowIdx, String nowKey, double fromNowTime, boolean forward);
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MicroLogic extends WaitLogic<PeakLogicDto.MicroPeakLogicDto> {


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
            return finder.getRMap().get(key);
        }

        @Override
        public boolean isBoundary(String fromKey, int nowIdx, String nowKey, double fromNowTime, boolean forward) {
            return fromNowTime > dto.peakMaxWaitSeconds;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class MacroLogic extends WaitLogic<PeakLogicDto.MacroPeakLogicDto> {


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
            if (r > dto.amplitudeRate) {
                return null;
            }
            return list.get(fidx);
        }

        @Override
        public double getValByKey(String key) {
            return finder.getRMap().get(key) - dto.baseVal;
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

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class BiggerLogic extends TsPeakLogic<PeakLogicDto.BiggerPeakLogicDto> {

        private TsHowBig _howBig = null;

        private TsHowBig hb() {
            if (_howBig == null) {
                _howBig = new TsHowBig(finder.getRMap());
            }
            return _howBig;
        }

        @Override
        public TsCurveUtils.Direction calcState(double maxNearTime, double minNearTime) {
            return null;
        }

        @Override
        public TsPeakFinder.MinMaxInfo filterAmplitude(TsPeakFinder.MinMaxInfo iArrays) {
            return null;
        }

        @Override
        public double getValByKey(String key) {
            return 0;
        }

        @Override
        public TsPeakFinder.PointType findMinOrMax(int idx) {
            return null;
        }
    }

}


