package net.bzk.infrastructure.tscurve.peak.logic;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.infrastructure.tscurve.TsCurveUtils;
import net.bzk.infrastructure.tscurve.dto.Point;
import net.bzk.infrastructure.tscurve.peak.PeakLogicDto;
import net.bzk.infrastructure.tscurve.peak.TsPeakFinder;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class MicroLogic extends WaitLogic<PeakLogicDto.MicroPeakLogicDto> {



    @Override
    public TsPeakFinder.MinMaxInfo filterAmplitude(TsPeakFinder.MinMaxInfo iArrays) {
        return iArrays;
    }

    @Override
    public double getValByKey(String key) {
        return finder.getRMap().get(key);
    }



    @Override
    public boolean isOverRange(int fromIdx, int idx) {
        String fromKey = getKeys().get(fromIdx);
        String key = getKeys().get(idx);
        double seconds = Math.abs(TsCurveUtils.subtractKeySeconds(fromKey, key));
        return seconds > dto.peakMaxWaitSeconds;
    }


}
