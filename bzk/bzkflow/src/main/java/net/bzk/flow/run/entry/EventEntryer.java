package net.bzk.flow.run.entry;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.dto.FlowRunEvent;
import net.bzk.flow.model.Condition;
import net.bzk.flow.model.Entry;
import net.bzk.flow.run.dao.EventListenerDao;
import net.bzk.infrastructure.JsonUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service("net.bzk.flow.model.Entry$EventEntry")
@Scope("prototype")
@Slf4j
public class EventEntryer extends Entryer<Entry.EventEntry> implements EventListenerDao.FlowEventListener {

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
        String aJson = JsonUtils.toJson(getModel().getCondition());
        var rp = BzkFlowUtils.replaceTextNotNull(k -> m.getByPath(k), aJson);
        Condition c = BzkFlowUtils.getFlowJsonMapper().readValue(rp, Condition.class);

    }
}
