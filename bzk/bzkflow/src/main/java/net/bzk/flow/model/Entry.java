package net.bzk.flow.model;

import java.time.temporal.ChronoUnit;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.infrastructure.convert.OType;

@Data
public class Entry implements OType {

	private String clazz;
	private String boxUid;
	private boolean autoRegister;


	public Entry() {
		clazz = this.getClass().getName();
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class FixedRateEntry extends Entry {

		private long period;
		private ChronoUnit unit;
		private long initialDelay;
		private String cronExpression;
		
	}
	
	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class PluginEntry extends Entry{
		
		private List<String> requiredKeys;
		private List<String> outputKeys;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class EventEntry extends Entry{

		private Condition condition;

	}

}
