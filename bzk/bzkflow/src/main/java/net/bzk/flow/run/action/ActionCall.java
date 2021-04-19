package net.bzk.flow.run.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.BsonDocument;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import lombok.Data;
import lombok.Getter;
import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.Constant;
import net.bzk.flow.model.Action;
import net.bzk.flow.model.Action.Polyglot;
import net.bzk.flow.model.var.VarLv;
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

	public <R> R parseScriptbleText(String plain) {
		if (plain.startsWith(Constant.SCRIPT_PREFIX)) {
			plain = plain.replaceFirst(Constant.SCRIPT_PREFIX, "");
			return (R) parseByStringCode(Polyglot.js.toString(), plain);
		}
		return (R) JsonUtils.stringToValue(plain);

	}

	public Object parseByStringCode(String polyglot, String ifCode) {
		logUtils.logActionCall(getUids(), ifCode);
		Object o = JsonUtils.stringToValue(ifCode);
		logUtils.logActionCall(getUids(), o.getClass() + " " + o);
		if (o instanceof String) {
			try {
				Object ans = callPolyglot(varQueryer, polyglot, o.toString());
				logUtils.logActionCall(getUids(), "callPolyglot " + ans.getClass() + " " + ans);
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

	public static void main(String[] args) throws IOException {
		try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
			Value function = context.eval("js", "const ans ={a:'b',c:[1,{a:'d'},3]}; ans;");
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
