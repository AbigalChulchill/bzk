package net.bzk.infrastructure.ordermgt;

import lombok.Data;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.tscurve.TsCurveFunc;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpMethod;

import java.time.ZonedDateTime;
import java.util.*;

public class OrderUtils {

    private static final OrderUtils instance = new OrderUtils();

    private OrderUtils() {
    }

    @Data
    public static class TestD {
        private Date d;
        private HttpMethod m;
    }

    public String test(Map t) {
        TestD td = JsonUtils.toByJson(t, TestD.class);
        return JsonUtils.toJson(td);
    }

    public boolean containsTags(String cid, List<String> trytags) {
        List<String> otags = parseTags(cid);
        for (String tg : otags) {
            if (trytags.contains(tg)) return true;
        }
        return false;
    }

    private List<String> parseTags(String cid) {
        if (!cid.startsWith("_")) return new ArrayList<>();
        if (!cid.endsWith("_")) return new ArrayList<>();
        cid = cid.substring(1, cid.length() - 1);
        return Arrays.asList(cid.split("-"));
    }


    public OrderSubtotal filter(String oods, String ftjson) {
        OrderFilter ft = JsonUtils.loadByJson(ftjson, OrderFilter.class);
        OrderDto.OrderDtoList l = JsonUtils.loadByJson(oods, OrderDto.OrderDtoList.class);
        OrderSubtotal ans = new OrderSubtotal();
        ans.setGroup(ft.getGroup());
        for (OrderDto ods : l) {
            if (ft.getOrigType() != null && !StringUtils.equals(ods.getOrigType(), ft.getOrigType())) continue;
            if (ft.getOrderType() != null && !StringUtils.equals(ods.getType(), ft.getOrderType())) continue;
            if (ft.getNotOrderType() != null && StringUtils.equals(ods.getType(), ft.getNotOrderType())) continue;
            if (ft.getSide() != null && !StringUtils.equals(ods.getSide(), ft.getSide())) continue;
            if (ft.getPositionSide() != null && !StringUtils.equals(ods.getPositionSide(), ft.getPositionSide())) continue;
            if (ft.getSymbol() != null && !StringUtils.equals(ft.getSymbol(), ods.getSymbol())) continue;
            if (ft.getStatus() != null && !StringUtils.equals(ft.getStatus(), ods.getStatus())) continue;
            if (ft.getTags() != null && !containsTags(ods.getClientOrderId(), ft.getTags())) continue;
            if (ft.getUntags() != null && containsTags(ods.getClientOrderId(), ft.getUntags())) continue;
            if (ft.getUpdateStartTime() != null && ods.parseUpdateAt().isBefore(ft.parseUpdateStartTime())) continue;
            if (ft.getUpdateEndTime() != null && ods.parseUpdateAt().isAfter(ft.parseUpdateEndTime())) continue;
            ans.getOrders().add(ods);
        }
        ans.subtotal();
        return ans;
    }

    public double getPrice(Map orderMap) {
        OrderDto dto = JsonUtils.toByJson(orderMap, OrderDto.class);
        if (dto.getAvgPrice() > 0) return dto.getAvgPrice();
        if (dto.getPrice() > 0) return dto.getPrice();
        if (dto.getStopPrice() > 0) return dto.getStopPrice();
        throw new NullPointerException(orderMap.toString());
    }

    public static OrderUtils getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        ZonedDateTime st = ZonedDateTime.parse("2015-08-18T00:00+01:00");
        ZonedDateTime ed = ZonedDateTime.parse("2019-08-18T00:00+01:00");
        System.out.println(ed.isBefore(st));
        System.out.println(ed.isAfter(st));
        System.out.println(st.isBefore(ed));
        System.out.println(st.isAfter(ed));

        String oStr = "{\\r\\n    \\\"lastAt\\\": \\\"2021-10-31T02:20:30.875000+00:00\\\",\\r\\n    \\\"origQty\\\": 1.128,\\r\\n    \\\"avgPrice\\\": 4530.936976950355,\\r\\n    \\\"orders\\\": [\\r\\n        {\\r\\n            \\\"clientOrderId\\\": \\\"_LOSS-SSwh-tkst_\\\",\\r\\n            \\\"cumQuote\\\": 0.0,\\r\\n            \\\"executedQty\\\": 0.0,\\r\\n            \\\"orderId\\\": 8389765509875807370,\\r\\n            \\\"origQty\\\": 0.127,\\r\\n            \\\"price\\\": 0.0,\\r\\n            \\\"reduceOnly\\\": true,\\r\\n            \\\"side\\\": \\\"BUY\\\",\\r\\n            \\\"status\\\": \\\"NEW\\\",\\r\\n            \\\"stopPrice\\\": 4665.11,\\r\\n            \\\"symbol\\\": \\\"ETHUSDT\\\",\\r\\n            \\\"timeInForce\\\": \\\"GTC\\\",\\r\\n            \\\"type\\\": \\\"STOP_MARKET\\\",\\r\\n            \\\"updateTime\\\": 1635646830875,\\r\\n            \\\"workingType\\\": \\\"CONTRACT_PRICE\\\",\\r\\n            \\\"avgPrice\\\": 0.0,\\r\\n            \\\"origType\\\": \\\"STOP_MARKET\\\",\\r\\n            \\\"positionSide\\\": \\\"SHORT\\\",\\r\\n            \\\"activatePrice\\\": null,\\r\\n            \\\"priceRate\\\": null,\\r\\n            \\\"closePosition\\\": false,\\r\\n            \\\"updateAt\\\": \\\"2021-10-31T02:20:30.875000+00:00\\\"\\r\\n        },\\r\\n        {\\r\\n            \\\"clientOrderId\\\": \\\"_GqwI-bneck-bot_\\\",\\r\\n            \\\"cumQuote\\\": 0.0,\\r\\n            \\\"executedQty\\\": 0.0,\\r\\n            \\\"orderId\\\": 8389765509844672234,\\r\\n            \\\"origQty\\\": 0.351,\\r\\n            \\\"price\\\": 4567.45,\\r\\n            \\\"reduceOnly\\\": false,\\r\\n            \\\"side\\\": \\\"SELL\\\",\\r\\n            \\\"status\\\": \\\"NEW\\\",\\r\\n            \\\"stopPrice\\\": 0.0,\\r\\n            \\\"symbol\\\": \\\"ETHUSDT\\\",\\r\\n            \\\"timeInForce\\\": \\\"GTC\\\",\\r\\n            \\\"type\\\": \\\"LIMIT\\\",\\r\\n            \\\"updateTime\\\": 1635584768618,\\r\\n            \\\"workingType\\\": \\\"CONTRACT_PRICE\\\",\\r\\n            \\\"avgPrice\\\": 0.0,\\r\\n            \\\"origType\\\": \\\"LIMIT\\\",\\r\\n            \\\"positionSide\\\": \\\"SHORT\\\",\\r\\n            \\\"activatePrice\\\": null,\\r\\n            \\\"priceRate\\\": null,\\r\\n            \\\"closePosition\\\": false,\\r\\n            \\\"updateAt\\\": \\\"2021-10-30T09:06:08.618000+00:00\\\"\\r\\n        },\\r\\n        {\\r\\n            \\\"clientOrderId\\\": \\\"_bneck-bot-njrD_\\\",\\r\\n            \\\"cumQuote\\\": 0.0,\\r\\n            \\\"executedQty\\\": 0.0,\\r\\n            \\\"orderId\\\": 8389765509844672199,\\r\\n            \\\"origQty\\\": 0.273,\\r\\n            \\\"price\\\": 4522.23,\\r\\n            \\\"reduceOnly\\\": false,\\r\\n            \\\"side\\\": \\\"SELL\\\",\\r\\n            \\\"status\\\": \\\"NEW\\\",\\r\\n            \\\"stopPrice\\\": 0.0,\\r\\n            \\\"symbol\\\": \\\"ETHUSDT\\\",\\r\\n            \\\"timeInForce\\\": \\\"GTC\\\",\\r\\n            \\\"type\\\": \\\"LIMIT\\\",\\r\\n            \\\"updateTime\\\": 1635584768513,\\r\\n            \\\"workingType\\\": \\\"CONTRACT_PRICE\\\",\\r\\n            \\\"avgPrice\\\": 0.0,\\r\\n            \\\"origType\\\": \\\"LIMIT\\\",\\r\\n            \\\"positionSide\\\": \\\"SHORT\\\",\\r\\n            \\\"activatePrice\\\": null,\\r\\n            \\\"priceRate\\\": null,\\r\\n            \\\"closePosition\\\": false,\\r\\n            \\\"updateAt\\\": \\\"2021-10-30T09:06:08.513000+00:00\\\"\\r\\n        },\\r\\n        {\\r\\n            \\\"clientOrderId\\\": \\\"_bneck-bot-brEo_\\\",\\r\\n            \\\"cumQuote\\\": 0.0,\\r\\n            \\\"executedQty\\\": 0.0,\\r\\n            \\\"orderId\\\": 8389765509844672161,\\r\\n            \\\"origQty\\\": 0.212,\\r\\n            \\\"price\\\": 4477.45,\\r\\n            \\\"reduceOnly\\\": false,\\r\\n            \\\"side\\\": \\\"SELL\\\",\\r\\n            \\\"status\\\": \\\"NEW\\\",\\r\\n            \\\"stopPrice\\\": 0.0,\\r\\n            \\\"symbol\\\": \\\"ETHUSDT\\\",\\r\\n            \\\"timeInForce\\\": \\\"GTC\\\",\\r\\n            \\\"type\\\": \\\"LIMIT\\\",\\r\\n            \\\"updateTime\\\": 1635584768406,\\r\\n            \\\"workingType\\\": \\\"CONTRACT_PRICE\\\",\\r\\n            \\\"avgPrice\\\": 0.0,\\r\\n            \\\"origType\\\": \\\"LIMIT\\\",\\r\\n            \\\"positionSide\\\": \\\"SHORT\\\",\\r\\n            \\\"activatePrice\\\": null,\\r\\n            \\\"priceRate\\\": null,\\r\\n            \\\"closePosition\\\": false,\\r\\n            \\\"updateAt\\\": \\\"2021-10-30T09:06:08.406000+00:00\\\"\\r\\n        },\\r\\n        {\\r\\n            \\\"clientOrderId\\\": \\\"_TrsA-bneck-bot_\\\",\\r\\n            \\\"cumQuote\\\": 0.0,\\r\\n            \\\"executedQty\\\": 0.0,\\r\\n            \\\"orderId\\\": 8389765509844672135,\\r\\n            \\\"origQty\\\": 0.165,\\r\\n            \\\"price\\\": 4433.12,\\r\\n            \\\"reduceOnly\\\": false,\\r\\n            \\\"side\\\": \\\"SELL\\\",\\r\\n            \\\"status\\\": \\\"NEW\\\",\\r\\n            \\\"stopPrice\\\": 0.0,\\r\\n            \\\"symbol\\\": \\\"ETHUSDT\\\",\\r\\n            \\\"timeInForce\\\": \\\"GTC\\\",\\r\\n            \\\"type\\\": \\\"LIMIT\\\",\\r\\n            \\\"updateTime\\\": 1635584768304,\\r\\n            \\\"workingType\\\": \\\"CONTRACT_PRICE\\\",\\r\\n            \\\"avgPrice\\\": 0.0,\\r\\n            \\\"origType\\\": \\\"LIMIT\\\",\\r\\n            \\\"positionSide\\\": \\\"SHORT\\\",\\r\\n            \\\"activatePrice\\\": null,\\r\\n            \\\"priceRate\\\": null,\\r\\n            \\\"closePosition\\\": false,\\r\\n            \\\"updateAt\\\": \\\"2021-10-30T09:06:08.304000+00:00\\\"\\r\\n        }\\r\\n    ],\\r\\n    \\\"group\\\": null,\\r\\n    \\\"groupMap\\\": {}\\r\\n}";
        oStr = StringEscapeUtils.unescapeJson(oStr);

        OrderFilter odf = new OrderFilter();
        


    }

}
