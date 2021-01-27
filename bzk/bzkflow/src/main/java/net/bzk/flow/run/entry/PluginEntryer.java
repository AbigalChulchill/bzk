package net.bzk.flow.run.entry;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Entry.PluginEntry;

@Service("net.bzk.flow.model.Entry$PluginEntry")
@Scope("prototype")
public class PluginEntryer extends Entryer<PluginEntry> {

	@Override
	protected void registerSchedule(TaskScheduler s) {

	}

	@Override
	public void unregister() {
		
	}

}
