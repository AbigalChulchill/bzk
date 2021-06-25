package net.bzk.flow.run.action;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.dto.FlowRunEvent;
import net.bzk.flow.dto.FlowRunEvent.EventData;
import net.bzk.flow.model.Action.EventAction;
import net.bzk.flow.model.var.VarValSet;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service("net.bzk.flow.model.Action$EventAction")
@Scope("prototype")
@Slf4j
public class EventActionCall extends ActionCall<EventAction> {

    @Inject
    private ApplicationEventPublisher applicationEventPublisher;

    public EventActionCall() {
        super(EventAction.class);
    }

    @Override
    public VarValSet call() throws Exception {
        Object d = getPolyglotEngine().parseScriptbleText(getModel().getData(), Object.class);
        EventData ed = new EventData(getUids(),d);
        FlowRunEvent e = new FlowRunEvent(ed);
        applicationEventPublisher.publishEvent(e);
        return new VarValSet();
    }
}
