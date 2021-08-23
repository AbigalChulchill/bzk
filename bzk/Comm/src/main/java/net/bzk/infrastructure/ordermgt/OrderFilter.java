package net.bzk.infrastructure.ordermgt;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class OrderFilter {
    private String symbol = null;
    private String side = null;
    private List<String> tags ;
    private List<String>  untags ;
    private String orderType ;
    private String notOrderType ;
    private String status ;
    private ZonedDateTime updateStartTime ;
    private ZonedDateTime updateEndTime ;
    private String group;
}
