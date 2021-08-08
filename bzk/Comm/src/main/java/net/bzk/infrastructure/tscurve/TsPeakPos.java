package net.bzk.infrastructure.tscurve;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bzk.infrastructure.ex.BzkRuntimeException;

public class TsPeakPos {

    public static enum Pos {
        A, B, C, D, E, F, G, H
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IGRate{
        private float investRate;
        private float guardRate;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {
        private IGRate sell;
        private IGRate buy;
        private Pos pos;
        private double h;
        private double lastV;
        private TsCurveUtils.Direction state;
    }

    private TsPeakFinder.Result rf;
    private TsPeakFinder.TrendInfo macro;
    private TsCurveUtils.Direction state;
    private double lastV;
    private double h;
    private double halfH;
    private Pos pos;

    public TsPeakPos(TsPeakFinder.Result pr) {
        rf = pr;
        macro = rf.getMacro();
        state = macro.getState();
        lastV = rf.getLast().getVal();
        h = getH();
        halfH = h / 2;
        pos = getPos();
    }

    private double getH() {
        if (state == TsCurveUtils.Direction.FALL) return Math.abs(macro.getNearMin().getVal());
        if (state == TsCurveUtils.Direction.RISE) return Math.abs(macro.getNearMax().getVal());
        throw new BzkRuntimeException("getH not support " + state);
    }

    private Pos getPos() {
        if (state == TsCurveUtils.Direction.RISE) {
            if (lastV <= 0) {
                return lastV <= -halfH ? Pos.A : Pos.B;
            } else {
                return lastV >= halfH ? Pos.D : Pos.C;
            }
        }
        if (state == TsCurveUtils.Direction.FALL) {
            if (lastV <= 0) {
                return lastV <= -halfH ? Pos.H : Pos.G;
            } else {
                return lastV >= halfH ? Pos.E : Pos.F;
            }
        }
        throw new BzkRuntimeException("getPos not support " + state + " v:" + lastV);
    }

    private float calcInvestRate(TsEnums.Side sb) {
        if (sb == TsEnums.Side.SELL) return (float) (lastV / h);
        if (sb == TsEnums.Side.BUY) return (float) (-lastV / h);
        throw new BzkRuntimeException("calcInvestRate not support " + state + " v:" + lastV);
    }

//    private float calcMiddleGuardRate() {
//        double s = Math.abs(lastV) - halfH;
//        let p = s / halfH;
//        return Math.pow(0.5, p);
//    }

//    function calcLongGroudRate() {
//        let absl = Math.abs(lastV);
//        let p = absl / h;
//        return Math.pow(0.5, p);
//    }
//
//    public Result calc() {
//
//    }

}
