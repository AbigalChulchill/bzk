package net.bzk.flow.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.model.var.VarMap;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Data
@EqualsAndHashCode(callSuper = false)
public class Flow extends BzkObj {

	private String name = "Flow";
	private Set<Box> boxs = new ConcurrentSkipListSet<>();
	private String varCfgUid;
	private VarMap vars = new VarMap();
	private Entry entry = new Entry();
	private String logEncryptKey = "1234567890123456";
	private ThreadCfg threadCfg = new ThreadCfg();

	@Data
	public static class ThreadCfg {
		private int corePoolSize = 10;
		private int maximumPoolSize = 50;
		private long keepAliveTime = 500;
		private TimeUnit aliveUnit = TimeUnit.MINUTES;
	}

	public Box findEntryBox() {
		return boxs.stream().filter(b -> StringUtils.equals(entry.getBoxUid(), b.getUid())).findFirst().get();
	}

	@JsonIgnore
	public ActionFindInfo getAction(String aUid) {
		for (Box b : boxs) {
			Optional<Action> ao = b.findAction(aUid);
			if (!ao.isPresent())
				continue;
			return new ActionFindInfo(b, ao.get());
		}
		throw new BzkRuntimeException("not find any action uid:" + aUid);
	}

	@JsonIgnore
	public List<Action> listAllActions() {
		List<Action> ans = new ArrayList<>();
		boxs.forEach(b -> ans.addAll(b.getActions()));
		return ans;
	}

	@Data
	@AllArgsConstructor(access = AccessLevel.PUBLIC)
	public static class ActionFindInfo {
		private Box box;
		private Action action;
	}

}
