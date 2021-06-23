package net.bzk.flow.run.entry;

import net.bzk.flow.dto.FlowRunEvent;
import net.bzk.flow.model.Entry;
import net.bzk.flow.run.dao.EventListenerDao;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service("net.bzk.flow.model.Entry$EventEntry")
@Scope("prototype")
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
	public void onEvent(FlowRunEvent e) {

	}
}
