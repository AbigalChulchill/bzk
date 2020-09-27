package net.bzk.flow.run.entry;

import java.time.ZonedDateTime;
import java.util.Date;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Entry.FixedRateEntry;

@Service("net.bzk.flow.model.flow.Entry$FixedRateEntry")
@Scope("prototype")
public class FixedRateEntryer extends Entryer<FixedRateEntry> {

	@Override
	protected void registerSchedule(TaskScheduler s) {
		ZonedDateTime zdt = ZonedDateTime.now().plus(getModel().getInitialDelay(), getModel().getInitUnit());
		s.schedule(this, Date.from(zdt.toInstant()));
	}

}
