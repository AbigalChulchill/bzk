package net.bzk.flow.run.action;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import net.bzk.flow.enums.Enums;
import org.apache.commons.lang3.RandomStringUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import lombok.Data;
import lombok.Getter;
import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.BzkConstant;
import net.bzk.flow.infra.PolyglotEngine;
import net.bzk.flow.model.Action;
import net.bzk.flow.enums.VarLv;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.service.FastVarQueryer;
import net.bzk.flow.run.service.RunLogService;
import net.bzk.flow.run.service.RunVarService;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;

public abstract class ActionCall<T extends Action> implements Callable<VarValSet> {
	@Getter
	private Class<T> modelClazz;
	@Getter
	private T model;
	@Getter
	private Uids uids;
	@Inject
	protected RunLogService logUtils;
	@Inject
	protected RunVarService varService;
	@Inject
	protected FastVarQueryer varQueryer;
	@Getter
	private PolyglotEngine polyglotEngine;

	public ActionCall(Class<T> c) {
		modelClazz = c;
	}

	@SuppressWarnings("rawtypes")
	public ActionCall initBase(Uids _uids, T a) {
		uids = _uids;
		uids.actionUid = a.getUid();
		uids.runActionUid = RandomStringUtils.randomAlphanumeric(BzkConstant.RUN_UID_SIZE);
		varQueryer.init(_uids);
		polyglotEngine = PolyglotEngine.FlowPolyglotEngine.builder().logUtils(logUtils).varQueryer(varQueryer).build();
		model = replaceModel(a);
		logUtils.log(uids, Enums.RunState.ModelReplaced, l -> {
			l.setMsg(JsonUtils.toJson(model));
		});
		return this;
	}

	protected T replaceModel(T _m) {
		return BzkFlowUtils.replaceModel(varQueryer, _m, modelClazz);
	}

	public RpcObj genRpcObj() {
		RpcObj ro = new RpcObj();
		ro.setHost("http://127.0.0.1:8080");
		ro.setUids(getUids());
		ro.setBoxVars(varService.getBoxDao().getVarMapByUid(uids.runBoxUid));
		ro.setFlowVars(varService.getFlowDao().getVarMapByUid(uids.runFlowUid));
		return ro;
	}









	public static void main(String[] args) throws IOException {
		try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
			Value function = context.eval("js", "Math.cbrt(0.2)");
			Object ov = BzkFlowUtils.fixPolyglotObj(function);
			System.out.println(ov);
			function = context.eval("js", "const g =[1,{a:'d',c:[1,2,3]},3]; g;");
			Object ov2 = BzkFlowUtils.fixPolyglotObj(function);
			System.out.println(ov2);
		}
	}

	@Data
	public static class Uids {
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
	public static class RpcObj {
		private Uids uids;
		private String host;
		private VarMap flowVars;
		private VarMap boxVars;
	}

}
