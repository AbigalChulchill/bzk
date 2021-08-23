package net.bzk.infrastructure.ordermgt;

import lombok.Data;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.tscurve.TsCurveFunc;
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


    public OrderSubtotal filter(List<Object> oods, Map ftm) {
        OrderFilter ft = JsonUtils.toByJson(ftm,OrderFilter.class);
        OrderDto.OrderDtoList l = JsonUtils.toByJson(oods, OrderDto.OrderDtoList.class);
        OrderSubtotal ans = new OrderSubtotal();
        ans.setGroup(ft.getGroup());
        for (OrderDto ods : l) {
            if (ft.getOrderType() != null && ods.getType() != ft.getOrderType()) continue;
            if (ft.getNotOrderType() != null && ods.getType() == ft.getNotOrderType()) continue;
            if (ft.getSide() != null && ods.getSide() != ft.getSide()) continue;
            if (ft.getSymbol() != null && ft.getSymbol() != ods.getSymbol()) continue;
            if (ft.getStatus() != null && ft.getStatus() != ods.getStatus()) continue;
            if (ft.getTags() != null && !containsTags(ods.getClientOrderId(), ft.getTags())) continue;
            if (ft.getUntags() != null && containsTags(ods.getClientOrderId(), ft.getUntags())) continue;
            if (ft.getUpdateStartTime() != null && ods.getUpdateAt().isBefore(ft.getUpdateStartTime())) continue;
            if (ft.getUpdateEndTime() != null && ods.getUpdateAt().isAfter(ft.getUpdateEndTime())) continue;
            ans.getOrders().add(ods);
        }
        ans.subtotal();
        return ans;
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
