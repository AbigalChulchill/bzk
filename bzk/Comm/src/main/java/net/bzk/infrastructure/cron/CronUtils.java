package net.bzk.infrastructure.cron;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import com.cronutils.model.CronType;
import com.cronutils.model.definition.CronDefinition;
import com.cronutils.model.definition.CronDefinitionBuilder;
import com.cronutils.model.time.ExecutionTime;
import com.cronutils.parser.CronParser;

public class CronUtils {

	public static final CronDefinition cronDefinition = CronDefinitionBuilder.instanceDefinitionFor(CronType.QUARTZ);
	public static final CronParser parser = new CronParser(cronDefinition);
	
	public static Optional<ZonedDateTime> getPassAt(String cron,ZonedDateTime now) {
		ExecutionTime executionTime = ExecutionTime.forCron(parser.parse(cron));
		return executionTime.lastExecution(now);
	}
	
	public static boolean inRange(String cron,int dulsencond,ZonedDateTime now) {
		Optional<ZonedDateTime> to = getPassAt(cron, now);
		if(!to.isPresent()) return false;
		ZonedDateTime st = to.get();
		ZonedDateTime et = st.plus(dulsencond, ChronoUnit.SECONDS);
		return et.isAfter(now);
	}
	
	

}
