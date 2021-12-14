package net.bzk.infrastructure.ordermgt;


import net.bzk.infrastructure.ex.BzkRuntimeException;

public class DirectionUtils {

    public static enum PositionSide {
        BOTH, LONG, SHORT, INVALID
    }

    public static enum OrderSide {
        BUY, SELL, INVALID
    }

    private static final DirectionUtils instance = new DirectionUtils();

    private DirectionUtils() {
    }

    public float getHighPrice(String positionSide, float a, float b) {
        switch (PositionSide.valueOf(positionSide)) {
            case LONG:
                return Math.max(a, b);
            case SHORT:
                return Math.min(a, b);
        }
        throw new BzkRuntimeException("not support " + positionSide);
    }

    public boolean isHighPrice(String positionSide, double base, double tar) {
        switch (PositionSide.valueOf(positionSide)) {
            case LONG:
                return base < tar;
            case SHORT:
                return base > tar;
        }
        throw new BzkRuntimeException("not support " + positionSide);
    }



    public OrderSide getLimitOrderSide(String positionSide) {
        switch (PositionSide.valueOf(positionSide)) {
            case LONG:
                return OrderSide.BUY;
            case SHORT:
                return OrderSide.SELL;
        }
        throw new BzkRuntimeException("not support " + positionSide);
    }

    public double plusPriceByRate(String positionSide,double orgPrice,double rate) {
        double dp = orgPrice * rate;
        switch (PositionSide.valueOf(positionSide)) {
            case LONG:
                return orgPrice + dp;
            case SHORT:
                return orgPrice - dp;
        }
        throw new BzkRuntimeException("not support " + positionSide);
    }



    public static DirectionUtils getInstance() {
        return instance;
    }
}
