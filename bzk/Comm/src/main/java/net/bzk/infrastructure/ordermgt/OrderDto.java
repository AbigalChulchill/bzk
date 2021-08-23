package net.bzk.infrastructure.ordermgt;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;

@Data
public class OrderDto {

    private String clientOrderId = "";
    private float cumQuote = 0.0f;
    private float executedQty = 0.0f;
    private String orderId = null;
    private float origQty = 0.0f;
    private float price = 0.0f;
    private boolean reduceOnly = false;
    private String side = null;
    private String status = null;
    private String stopPrice = null;
    private String symbol = "";
    private String timeInForce = null;
    private String type = null;
    private long updateTime = 0;
    private String workingType = "";
    private float avgPrice = 0.0f;
    private String origType = "";
    private String positionSide = "";
    private String activatePrice = null;
    private String priceRate = null;
    private String closePosition = null;
    private ZonedDateTime updateAt;

    public static class OrderDtoList extends ArrayList<OrderDto> {
    }

}
