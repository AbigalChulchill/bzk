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
    public static class IGRate {
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

    private float calcMiddleGuardRate() {
        double s = Math.abs(lastV) - halfH;
        double p = (s / halfH);
        return (float) Math.pow(0.5, p);
    }

    private float calcLongGroudRate() {
        double absl = Math.abs(lastV);
        double p = absl / h;
        return (float) Math.pow(0.5, p);
    }


    private float calcGroudRate(TsEnums.Side sb) {
        switch (pos) {
            case A:
                return sb == TsEnums.Side.BUY ? calcMiddleGuardRate() : calcLongGroudRate();
            case B:
                return sb == TsEnums.Side.BUY ? calcMiddleGuardRate() : 0;
            case C:
                return sb == TsEnums.Side.BUY ? calcMiddleGuardRate() : 0;
            case D:
                return sb == TsEnums.Side.BUY ? calcMiddleGuardRate() : calcLongGroudRate();
            case E:
                return sb == TsEnums.Side.BUY ? calcLongGroudRate() : calcMiddleGuardRate();
            case F:
                return sb == TsEnums.Side.BUY ? 0 : calcMiddleGuardRate();
            case G:
                return sb == TsEnums.Side.BUY ? 0 : calcMiddleGuardRate();
            case H:
                return sb == TsEnums.Side.BUY ? calcLongGroudRate() : calcMiddleGuardRate();
        }
        throw new BzkRuntimeException("not support " + state + " v:" + lastV + "POS:" + pos);
    }


    public Result calc() {
        return Result.builder()
                .buy(
                        IGRate.builder()
                                .investRate(calcInvestRate(TsEnums.Side.BUY))
                                .guardRate(calcGroudRate(TsEnums.Side.BUY))
                                .build()
                )
                .sell(
                        IGRate.builder()
                                .investRate(calcInvestRate(TsEnums.Side.SELL))
                                .guardRate(calcGroudRate(TsEnums.Side.SELL))
                                .build()
                )

    }

}
