package net.bzk.infrastructure.tscurve.peak.logic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.infrastructure.tscurve.TsCurveUtils;
import net.bzk.infrastructure.tscurve.dto.Point;
import net.bzk.infrastructure.tscurve.peak.PeakLogicDto;
import net.bzk.infrastructure.tscurve.peak.TsPeakFinder;
import net.bzk.infrastructure.tscurve.peak.TsPeakLogic;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class WaitLogic<E extends PeakLogicDto.WaitPeakLogicDto> extends TsPeakLogic<E> {


    public TsPeakFinder.PointType findMinOrMax(int idx) {
        List<Point> slicePoints = listSliceRange(idx);
        if (slicePoints.size() <= 0) return TsPeakFinder.PointType.NONE;
        var maxP = slicePoints.stream().max((a, b) -> Double.compare(a.val, b.val)).get();
        var minP = slicePoints.stream().min((a, b) -> Double.compare(a.val, b.val)).get();
        boolean isMax = isMax(idx, maxP);
        boolean isMin = isMin(idx, minP);
        if (isMax && isMin) return TsPeakFinder.PointType.NONE;
        if (isMax) return TsPeakFinder.PointType.MAXED;
        if (isMin) return TsPeakFinder.PointType.MINED;
        return TsPeakFinder.PointType.NONE;
    }

    protected boolean isMax(int idx, Point maxP) {
        return maxP.idx == idx;
    }

    protected boolean isMin(int idx, Point minP) {
        return minP.idx == idx;
    }

    public List<Point> listSliceRange(int idx) {
        List<Point> ans = new ArrayList<>();
        ans.addAll(scanRange(idx, true));
        ans.add(genPoint(idx));
        ans.addAll(scanRange(idx, false));
        return ans;
    }

    private List<Point> scanRange(int idx, boolean forward) {
        int formIdx = idx;
        List<Point> ans = new ArrayList<>();
        while (!isBoundary(idx,forward)) {
            idx += forward ? -1 : +1;
            if (isOverRange(formIdx, idx)) return ans;
            ans.add(genPoint(idx));
        }
        return ans;

    }

    public abstract boolean isOverRange(int fromIdx, int idx);

    private boolean isBoundary(int idx, boolean forward) {
        idx += forward ? -1 : +1;
        if (idx < 0) return true;
        if (idx >= getKeys().size() ) return true;
        return false;
    }


    public TsCurveUtils.Direction calcState(double maxNearTime, double minNearTime) {
        if (maxNearTime < minNearTime) {
            return maxNearTime >= dto.peakMaxWaitSeconds ? TsCurveUtils.Direction.FALL : TsCurveUtils.Direction.RISE;
        } else {
            return minNearTime >= dto.peakMaxWaitSeconds ? TsCurveUtils.Direction.RISE : TsCurveUtils.Direction.FALL;
        }
    }


}
