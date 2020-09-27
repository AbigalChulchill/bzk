package net.bzk.flow.model;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.model.var.BaseVar;

@Data
@EqualsAndHashCode(callSuper = false)
public class Flow extends BzkObj {

	private String name;
	private Set<Box> boxs;
	private BaseVar vars = new BaseVar();
	private Entry entry ;
	private ThreadCfg threadCfg = new ThreadCfg();
	
	@Data
	public static class ThreadCfg{
		private int corePoolSize=10;
		private int maximumPoolSize=50;
		private  long keepAliveTime=500;
		private TimeUnit aliveUnit=TimeUnit.MINUTES;
	}

	public Box findEntryBox() {
		return boxs.stream()
				.filter(b -> StringUtils.equals(entry.getBoxUid(), b.getUid()))
				.findFirst().get();
	}

}
