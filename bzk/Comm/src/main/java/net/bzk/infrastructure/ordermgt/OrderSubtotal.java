package net.bzk.infrastructure.ordermgt;

import lombok.Data;
import net.bzk.infrastructure.ReflectionTool;
import net.bzk.infrastructure.ex.BzkRuntimeException;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

@Data
public class OrderSubtotal {
    private Date lastAt;
    private float executedQty;
    private String group;
    private Map<String, OrderSubtotal> groupMap = new HashMap<>();
    private List<OrderDto> orders = new ArrayList<>();

    public void subtotal() {
        if (orders.size() <= 0) return;
        orders.sort((a, b) -> b.getUpdateAt().compareTo(a.getUpdateAt()));
        executedQty = (Float) orders.stream().map(o -> o.getExecutedQty()).reduce(0f, (a, b) -> a + b);
        if (StringUtils.isBlank(group)) return;
        try {
            setupGroup();
        } catch (IllegalAccessException e) {
            throw new BzkRuntimeException(e);
        }
    }

    private void setupGroup() throws IllegalAccessException {
        for (OrderDto od : orders) {
            String groupv = (String) ReflectionTool.getValue(od, group);
            OrderSubtotal gSubBundle = getGroupSubtotal(groupv);
            gSubBundle.getOrders().add(od);
        }
        for (var v : groupMap.values()) {
            v.subtotal();
        }
    }

    private OrderSubtotal getGroupSubtotal(String k) {
        if (groupMap.containsKey(k)) {
            groupMap.put(k, new OrderSubtotal());
        }
        return groupMap.get(k);
    }

}
