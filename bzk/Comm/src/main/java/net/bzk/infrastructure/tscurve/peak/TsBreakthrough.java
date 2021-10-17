package net.bzk.infrastructure.tscurve.peak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.PropertyUtilsBean;

public class TsBreakthrough {




    public TsBreakthrough(){
    }

    public Result calc(TsPeakFinder.TrendInfo info){
        return null;
    }

    public static enum Kind{
        BREAKTHROUGH,HOLD_ON
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result{

        private Kind kind;
        private boolean isCurrentBreaking;

    }



}
