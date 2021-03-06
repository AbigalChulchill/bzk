package net.bzk.infrastructure.ordermgt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.NotImplementedException;

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
    private float stopPrice = 0.0f;
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
    private String updateAt;

    @JsonIgnore
    public ZonedDateTime parseUpdateAt() {
        return java.time.ZonedDateTime.parse(updateAt);
    }

    @JsonIgnore
    public float optPrice() {
        if (avgPrice > 0) return avgPrice;
        if (price > 0) return price;
        if (stopPrice > 0) return stopPrice;
        throw new NotImplementedException("not support this order :" + orderId);
    }

    public static class OrderDtoList extends ArrayList<OrderDto> {
    }

}
