package net.bzk.flow.dto;

import lombok.Data;
import net.bzk.flow.run.action.ActionCall.Uids;
import org.springframework.context.ApplicationEvent;

public class FlowRunEvent extends ApplicationEvent {

    public FlowRunEvent(EventData source) {
        super(source);
    }

    public EventData getData() {
        return (EventData) getSource();
    }

    @Data
    public static class EventData {
        private Uids uids;
        private Object data;
    }
}
