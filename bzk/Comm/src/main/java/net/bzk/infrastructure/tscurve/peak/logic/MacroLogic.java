package net.bzk.infrastructure.tscurve.peak.logic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.infrastructure.tscurve.dto.Point;
import net.bzk.infrastructure.tscurve.peak.PeakLogicDto;
import net.bzk.infrastructure.tscurve.peak.TsPeakFinder;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MacroLogic extends WaitLogic<PeakLogicDto.MacroPeakLogicDto> {


    @Override
    public boolean isOverRange(int fromIdx, int idx) {
        var fromV = getValByKey(getKeys().get(fromIdx));
        var toV = getValByKey(getKeys().get(idx));
        if (fromV < 0) {
            return toV > 0;
        } else {
            return toV < 0;
        }
    }

    @Override
    protected boolean isMax(int idx, Point maxP) {
        var fromV = getValByKey(getKeys().get(idx));
        if (fromV < 0) return false;
        return super.isMax(idx, maxP);
    }

    @Override
    protected boolean isMin(int idx, Point minP) {
        var fromV = getValByKey(getKeys().get(idx));
        if (fromV > 0) return false;
        return super.isMin(idx, minP);
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

    private List<Point> listAmplitudeSmall(int idx, List<Point> list) {
        List<Point> ans = new ArrayList<>();
        double curV = Math.abs(list.get(idx).getVal());
        boolean forward = true, later = true;

        for (int i = 1; i < list.size(); i++) {
            Point fr = getRmForward(forward, idx, i, curV, list);
            if (fr == null) forward = false;
            Point lr = getRmLater(later, idx, i, curV, list);
            if (lr == null) later = false;
            if (fr != null) ans.add(fr);
            if (lr != null) ans.add(lr);
        }

        return ans;
    }

    private Point getRmLater(boolean later, int idx, int pidx, double curV, List<Point> list) {
        if (!later) return null;
        int fidx = idx + pidx;
        if (fidx >= list.size()) {
            return null;
        }
        return checkOveAmplitudeRate(list, fidx, curV);

    }

    private Point getRmForward(boolean forward, int idx, int pidx, double curV, List<Point> list) {
        if (!forward) return null;
        int fidx = idx - pidx;
        if (fidx < 0) {
            return null;
        }
        return checkOveAmplitudeRate(list, fidx, curV);
    }

    private Point checkOveAmplitudeRate(List<Point> list, int fidx, double curV) {
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

}
