package net.bzk.infrastructure.ordermgt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class OrderFilter {
    private String symbol = null;
    private String positionSide = null;
    private String side = null;
    private List<String> tags ;
    private List<String>  untags ;
    private String orderType ;
    private String notOrderType ;
    private String origType;
    private String status ;
    private String updateStartTime ;
    private String updateEndTime ;
    private String group;

    @JsonIgnore
    public ZonedDateTime parseUpdateStartTime(){
        return java.time.ZonedDateTime.parse(updateStartTime);
    }

    @JsonIgnore
    public ZonedDateTime parseUpdateEndTime(){
        return java.time.ZonedDateTime.parse(updateEndTime);
    }
}
