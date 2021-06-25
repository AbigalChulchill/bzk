package net.bzk.flow.run.entry;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.dto.FlowRunEvent;
import net.bzk.flow.infra.PolyglotEngine;
import net.bzk.flow.model.Condition;
import net.bzk.flow.model.Entry;
import net.bzk.flow.run.dao.EventListenerDao;
import net.bzk.flow.run.flow.Conditioner;
import net.bzk.infrastructure.JsonUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service("net.bzk.flow.model.Entry$EventEntry")
@Scope("prototype")
@Slf4j
public class EventEntryer extends Entryer<Entry.EventEntry> implements EventListenerDao.FlowEventListener {

    @Inject
    protected ApplicationContext context;
    @Inject
    private EventListenerDao eventListenerDao;

    @Override
    protected void registerSchedule(TaskScheduler s) {
        eventListenerDao.add(this);
    }

    @Override
    public void unregister() {
        eventListenerDao.remove(this);
    }

    @Override
    public void onEvent(FlowRunEvent e) throws Exception {
        var m = e.getData().toJsonMap();
        PolyglotEngine pe = new PolyglotEngine();
        var c = Conditioner.initConditioner(context,pe,s->m.getByPath(s),getModel().getCondition());
        if (c.isTrue()){
            run();
        }
    }
}
