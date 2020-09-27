package net.bzk.flow.run.action;

import java.util.concurrent.Callable;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Data;
import lombok.Getter;
import net.bzk.flow.model.Action;
import net.bzk.flow.model.var.VarValSet;


public abstract class ActionCall<T extends Action> implements Callable<VarValSet> {
	
	@Getter
	private T model;
	@Getter
	private Uids uids;
	
	@SuppressWarnings("rawtypes")
	public ActionCall initBase(Uids _uids,T a) {
		uids = _uids;
		uids.actionUid = model.getUid();
		uids.runActionUid=RandomStringUtils.randomAlphanumeric(32);
		model = a;
		return this;
	}
	
	@Data
	public static class Uids{
		private String flowUid;
		private String runFlowUid;
		private String boxUid;
		private String runBoxUid;
		private String actionUid;
		private String runActionUid;
		
	}
	
	@Data
	public static class RpcObj{
		private Uids uids;
		private String host;
	}
	
	



}
