package net.bzk.flow.run.entry;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Entry.FixedRateEntry;

@Service("net.bzk.flow.model.Entry$FixedRateEntry")
@Scope("prototype")
public class FixedRateEntryer extends Entryer<FixedRateEntry> {

	private ScheduledFuture scheduledFuture;
	
	@Override
	protected void registerSchedule(TaskScheduler s) {
		ZonedDateTime zdt = ZonedDateTime.now().plus(getModel().getInitialDelay(), getModel().getUnit());
		Date d = Date.from(zdt.toInstant());
		if (getModel().getPeriod() <= 0) {
			scheduledFuture = s.schedule(this, d);
		}else {
			Duration dr= Duration.of(getModel().getPeriod(), getModel().getUnit());
			scheduledFuture = s.scheduleAtFixedRate(this, zdt.toInstant(), dr);
		}

	}

	@Override
	public void unregister() {
		scheduledFuture.cancel(true);		
	}

}
