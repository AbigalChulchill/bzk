package net.bzk.flow.dto;

import lombok.Data;
import net.bzk.flow.run.flow.FlowRuner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class JobRunInfo {
    private int allCount;
    private int runCount;
    private int archiveCount;
    private Map<FlowRuner.State, Integer> stateCounts = new HashMap<>();
    private FlowRuner.State lastState;
    private String uid;
    private String name;
    private boolean enable;
    private Date lastStartAt;

    TODO sha1 saved model and run model

}
