package net.bzk.flow.run.action;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;

import lombok.Data;
import lombok.Getter;
import net.bzk.flow.Constant;
import net.bzk.flow.model.Action;
import net.bzk.flow.model.var.VarLv;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.utils.LogUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;


public abstract class ActionCall<T extends Action> implements Callable<VarValSet> {
	
	@Getter
	private T model;
	@Getter
	private Uids uids;
	@Inject
	protected LogUtils logUtils;
	
	@SuppressWarnings("rawtypes")
	public ActionCall initBase(Uids _uids,T a) {
		uids = _uids;
		model = a;
		uids.actionUid = model.getUid();
		uids.runActionUid=RandomStringUtils.randomAlphanumeric(Constant.RUN_UID_SIZE);
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
		
		
		public String getLvUid(VarLv point) {
			switch (point) {
			case not_specify:
			case run_box:
				return getRunBoxUid();
			case run_flow:
				return getRunFlowUid();
			}
			throw new BzkRuntimeException("not supporty type:" + point);
		}
		
	}
	
	@Data
	public static class RpcObj{
		private Uids uids;
		private String host;
	}
	
	



}
