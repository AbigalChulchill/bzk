package net.bzk.flow.model;

import java.time.temporal.ChronoUnit;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.model.Condition.Val;
import net.bzk.infrastructure.convert.OType;

@Data
public class Entry implements OType {

	private String clazz;
	private String boxUid;

	public Entry() {
		clazz = this.getClass().getName();
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class FixedRateEntry extends Entry {

		private long period;
		private ChronoUnit initUnit;
		private long initialDelay; 
		
	}

}
