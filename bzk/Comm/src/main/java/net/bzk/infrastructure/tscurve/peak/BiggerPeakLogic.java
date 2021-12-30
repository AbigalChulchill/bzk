package net.bzk.infrastructure.tscurve.peak;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.infrastructure.convert.Overwrite;
import net.bzk.infrastructure.convert.OverwriteIncludeClass;
import net.bzk.infrastructure.tscurve.TsCurveUtils;
import net.bzk.infrastructure.tscurve.TsHowBig;
import net.bzk.infrastructure.tscurve.dto.Point;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class BiggerPeakLogic extends TsPeakLogic<PeakLogicDto.BiggerPeakLogicDto> {

    private TsHowBig _howBig = null;
    private TsBiggerFinder _biggerFinder;
    private Map<String, Double> cacheDeepValMap = new HashMap<>();

    private TsHowBig hb() {
        if (_howBig == null) {
            _howBig = new TsHowBig(finder.getRMap());
            _biggerFinder = new TsBiggerFinder(finder.getRMap());
        }
        return _howBig;
    }

    @Override
    public TsCurveUtils.Direction calcState(double maxNearTime, double minNearTime) {
        boolean isNearMin = minNearTime < maxNearTime;
        double nearTime = isNearMin ? minNearTime : maxNearTime;
        var r = getReversePeak(maxNearTime, minNearTime);
        boolean findded = r.getState() == TsBiggerFinder.FilterState.FINDED;
        boolean isOverWaitTime = nearTime > dto.reverseWaitTime;
        if (findded || isOverWaitTime) {
            return isNearMin ? TsCurveUtils.Direction.RISE : TsCurveUtils.Direction.FALL;
        } else {
            return isNearMin ? TsCurveUtils.Direction.FALL : TsCurveUtils.Direction.RISE;
        }

    }

    private TsBiggerFinder.Result getReversePeak(double maxNearTime, double minNearTime) {
        boolean isNearMin = minNearTime < maxNearTime;
        double nearTime = isNearMin ? minNearTime : maxNearTime;
        return _biggerFinder.calc(isNearMin, nearTime, dto.reversePersistTime);
    }

    @Override
    public TsPeakFinder.MinMaxInfo filterAmplitude(TsPeakFinder.MinMaxInfo iArrays) {

        boolean maxed = false;
        var all = iArrays.getAll();
        List<Point> rmAllList = new ArrayList<>();

        DeepPoint curPeakPoint = null;
        for (int i = 0; i < all.size(); i++) {
            DeepPoint dp = (DeepPoint) all.get(i);
            if ("2021-09-22T00:00:00+00:00".equals(dp.getKey())) {
                System.out.println(dp);
            }

            if (i == 0) {
                maxed = dp.deepTimeVal > 0;
                curPeakPoint = dp;
                continue;
            }
            if ((maxed && dp.deepTimeVal < 0) || !maxed && dp.deepTimeVal > 0) {
                maxed = dp.deepTimeVal > 0;
                curPeakPoint = dp;
                continue;
            }
            if (Math.abs(dp.deepTimeVal) > Math.abs(curPeakPoint.deepTimeVal)) {
                rmAllList.add(curPeakPoint);
                curPeakPoint = dp;
            } else {
                rmAllList.add(dp);
            }

        }

        for (var rp : rmAllList) {
            removeByIdx(iArrays.getAll(), rp.getIdx());
            removeByIdx(iArrays.getMax(), rp.getIdx());
            removeByIdx(iArrays.getMin(), rp.getIdx());
        }
        return iArrays;

    }

    @Override
    public double getValByKey(String key) {
        if (!cacheDeepValMap.containsKey(key)) {
            var bigR = hb().calc(TsHowBig.Dto.builder()
                    .bigger(true)
                    .targetKey(key)
                    .build());
            var smallR = hb().calc(TsHowBig.Dto.builder()
                    .bigger(false)
                    .targetKey(key)
                    .build());
            if (bigR.getTime() > smallR.getTime()) {
                cacheDeepValMap.put(key, bigR.getTime());
            } else {
                cacheDeepValMap.put(key, -smallR.getTime());
            }
        }

        double ans = cacheDeepValMap.get(key);
        if ("2021-09-22T00:00:00+00:00".equals(key)) {
            System.out.println("OO");
        }
        return ans;
    }

    @Override
    public TsPeakFinder.PointType findMinOrMax(int idx) {

        String origKey = getKeys().get(idx);
        String forwardKey = optKey(idx - 1);
        String backKey = optKey(idx + 1);
        var origVal = getValByKey(origKey);
        if (Math.abs(origVal) < dto.persistTime) return TsPeakFinder.PointType.NONE;

        boolean forwardCheck = isPeak(origKey, forwardKey);
        boolean backCheck = isPeak(origKey, backKey);
        if (forwardCheck && backCheck) {
            return origVal > 0 ? TsPeakFinder.PointType.MAXED : TsPeakFinder.PointType.MINED;
        }
        return TsPeakFinder.PointType.NONE;
    }

    private boolean isPeak(String origKey, String checkKey) {
        if (StringUtils.isBlank(checkKey)) {
            return true;
        }
        var origVal = getValByKey(origKey);
        var checkVal = getValByKey(checkKey);
        if ((origVal * checkVal) < 0) return true;
        return Math.abs(origVal) > Math.abs(checkVal);
    }

    private String optKey(int nidx) {
        if (nidx < 0) return null;
        if (nidx >= getKeys().size()) return null;
        return getKeys().get(nidx);
    }

    @Override
    public Point genPoint(int i) {
        String key = getKeys().get(i);
        DeepPoint ans = new DeepPoint();
        ans.setDeepTimeVal(getValByKey(key));
        ans.setDeepTimeDayVal(ans.getDeepTimeVal() / (60 * 60 * 24));
        ans.setIdx(i);
        ans.setDtime(TsCurveUtils.subtractKeySeconds(finder.getFirstKey(), key));
        ans.setKey(key);
        ans.setVal(finder.getRMap().get(key));
        return ans;
    }

    @Override
    public TsPeakFinder.Result fixResult(TsPeakFinder.Result orig) {
        BiggerResult newAns = new BiggerResult();
        newAns = Overwrite.overwrite(orig, newAns);
        newAns.setReverseResult(getReversePeak(orig.getTrendInfo().getMaxNearTime(), orig.getTrendInfo().getMinNearTime()));
        return newAns;
    }

    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class DeepPoint extends Point {
        public double deepTimeVal;
        private double deepTimeDayVal;
    }


    @Data
    @EqualsAndHashCode(callSuper = false)
    @OverwriteIncludeClass(include = TsPeakFinder.Result.class)
    public static class BiggerResult extends TsPeakFinder.Result {
        private TsBiggerFinder.Result reverseResult;
    }
}
