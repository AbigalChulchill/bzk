package net.bzk.flow.run.action;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.slf4j.Logger;

import lombok.Data;
import lombok.Getter;
import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.Constant;
import net.bzk.flow.model.Action;
import net.bzk.flow.model.var.VarLv;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.service.FastVarQueryer;
import net.bzk.flow.run.service.RunVarService;
import net.bzk.flow.utils.LogUtils;
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
	protected LogUtils logUtils;
	@Inject
	protected RunVarService varService;
	@Inject
	protected FastVarQueryer varQueryer;

	public ActionCall(Class<T> c) {
		modelClazz = c;
	}

	@SuppressWarnings("rawtypes")
	public ActionCall initBase(Uids _uids, T a) {
		uids = _uids;
		uids.actionUid = a.getUid();
		uids.runActionUid = RandomStringUtils.randomAlphanumeric(Constant.RUN_UID_SIZE);
		varQueryer.init(_uids);
		model = replaceModel(a);
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

	public static Object callPolyglot(FastVarQueryer varQueryer, String polyglot, String code) {
		try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
			context.getBindings(polyglot).putMember("bzk", varQueryer);
			Value function = context.eval(polyglot, code);
			Object ans = function.as(Object.class);
			return fixListObj(ans, function);
		}
	}

	public Object parseByStringCode( String polyglot, String ifCode) {
		logUtils.logActionCall( getUids(), ifCode);
		Object o = JsonUtils.stringToValue(ifCode);
		logUtils.logActionCall( getUids(), o.getClass() + " " + o);
		if (o instanceof String) {
			try {
				Object ans = callPolyglot(varQueryer, polyglot, o.toString());
				logUtils.logActionCall( getUids(), "callPolyglot " + ans.getClass() + " " + ans);
				return ans;
			} catch (Exception e) {
				logUtils.logActionCallWarn(this, e.getMessage());
			}
		}
		return o;
	}

	public static Object fixListObj(Object ans, Value function) {
		if (ans instanceof Map) {
			if (((Map) ans).size() > 0) {
				return JsonUtils.toByJson(ans, Object.class);
			}
			if (function.getArraySize() > 0) {
				ans = function.as(List.class);
				return JsonUtils.toByJson(ans, Object.class);
			}
		}
		return JsonUtils.toByJson(ans, Object.class);
	}

	public static void main(String[] args) {
		try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
			Value function = context.eval("js", "const ans ={a:'b',c:[1,{a:'d'},3]}; ans;");
			Object ans = function.as(Object.class);
			System.out.println(ans.getClass() + " " + ans.toString());
			String js = JsonUtils.toJson(ans);
			System.out.println(js);
			function = context.eval("js", "const g =[1,{a:'d',c:[1,2,3]},3]; g;");
			List l = function.as(List.class);
			System.out.println(l.getClass() + " " + l.toString());
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
