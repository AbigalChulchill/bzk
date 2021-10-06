package net.bzk.infrastructure.ordermgt;

import lombok.Data;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.tscurve.TsCurveFunc;
import org.apache.commons.lang3.StringUtils;
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
    }

}
