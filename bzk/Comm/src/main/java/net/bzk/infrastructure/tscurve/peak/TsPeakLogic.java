package net.bzk.infrastructure.tscurve.peak;

import lombok.*;
import net.bzk.infrastructure.tscurve.TsCurveUtils;
import net.bzk.infrastructure.tscurve.dto.Point;
import net.bzk.infrastructure.tscurve.peak.logic.WaitLogic;

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


    protected void removeByIdx(List<Point> list, int idx) {
        var po = list.stream().filter(p -> p.getIdx() == idx).findFirst();
        if (po.isEmpty()) return;
        list.remove(po.get());
    }

    public Point genPoint(int i) {
        String key = getKeys().get(i);
        return Point.builder().key(key).idx(i).val(finder.getRMap().get(key)).dtime(TsCurveUtils.subtractKeySeconds(finder.getFirstKey(), key)).build();
    }

    public TsPeakFinder.Result fixResult(TsPeakFinder.Result ans) {
        return ans;
    }

    public abstract TsCurveUtils.Direction calcState(double maxNearTime, double minNearTime);


    public abstract TsPeakFinder.MinMaxInfo filterAmplitude(TsPeakFinder.MinMaxInfo iArrays);

    public abstract double getValByKey(String key);

    public abstract TsPeakFinder.PointType findMinOrMax(int idx);




}


