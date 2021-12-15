package net.bzk.infrastructure.tscurve.tsdb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FluxPoint {
    private String measurement;
    private Map<String, String> tags;
    private String field;
    private double val;
    private ZonedDateTime time;


}
