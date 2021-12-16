package net.bzk.flow.run.entry;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Entry.FixedRateEntry;

@Service("net.bzk.flow.model.Entry$FixedRateEntry")
@Scope("prototype")
public class FixedRateEntryer extends Entryer<FixedRateEntry> {

    private ScheduledFuture scheduledFuture;

    @Override
    protected void registerSchedule(TaskScheduler s) {

        if (StringUtils.isNotBlank(getModel().getCronExpression())) {
            scheduledFuture = s.schedule(this, new CronTrigger(getModel().getCronExpression()));
            return;
        }

        ZonedDateTime zdt = ZonedDateTime.now().plus(getModel().getInitialDelay(), getModel().getUnit());
        Date d = Date.from(zdt.toInstant());
        if (getModel().getPeriod() <= 0) {
            scheduledFuture = s.schedule(this, d);
        } else {
            Duration dr = Duration.of(getModel().getPeriod(), getModel().getUnit());
            scheduledFuture = s.scheduleAtFixedRate(this, zdt.toInstant(), dr);
        }

    }


//    public static class CronExpTrigger implements Trigger {
//        private String cronExpression;
//
//        public CronExpTrigger(String cronExpression) {
//            this.cronExpression = cronExpression;
//        }
//
//        @Override
//        public Date nextExecutionTime(TriggerContext triggerContext) {
//            CronTrigger crontrigger = new CronTrigger(cronExpression);
//            return crontrigger.nextExecutionTime(triggerContext);
//        }
//
//    }

    @Override
    public void unregister() {
        scheduledFuture.cancel(true);
    }

}
