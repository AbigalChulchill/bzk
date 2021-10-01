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

    public boolean isValidStopPrice(String positionSide, double base, double stop) {
        switch (PositionSide.valueOf(positionSide)) {
            case LONG:
                return base > stop;
            case SHORT:
                return base < stop;
        }
        throw new BzkRuntimeException("not support " + positionSide);
    }

    public boolean isValidLimitPrice(String positionSide, double base, double limit) {
        switch (PositionSide.valueOf(positionSide)) {
            case LONG:
                return base < limit;
            case SHORT:
                return base > limit;
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


    public static DirectionUtils getInstance() {
        return instance;
    }
}
