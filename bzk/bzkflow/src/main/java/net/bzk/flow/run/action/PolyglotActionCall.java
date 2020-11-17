package net.bzk.flow.run.action;

import java.awt.List;
import java.util.Map;

import javax.inject.Inject;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.model.Action.PolyglotAction;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.service.FastVarQueryer;
import net.bzk.flow.utils.LogUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.JsonUtils.DataType;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service("net.bzk.flow.model.Action$PolyglotAction")
@Scope("prototype")
@Slf4j
public class PolyglotActionCall extends ActionCall<PolyglotAction> {
	@Inject
	private FastVarQueryer fastQueryer;

	@Override
	public VarValSet call() throws Exception {
		VarValSet set = new VarValSet();
		fastQueryer.init(getUids());
		try (Context context = Context.create()) {
			Value v = context.getBindings(getModel().getPolyglot().toString());
			v.putMember("bzk", fastQueryer);
			Value function = context.eval(getModel().getPolyglot().toString(), getModel().getCode());
//			if (!function.canExecute()) {
//				throw new BzkRuntimeException("Not Execute:" + JsonUtils.toJson(getUids()));
//			}
			Object ans = executeByType(function, getModel().getResultType());
			logUtils.logActionCall(log, getUids(), "get ans {0}", ans);
			if (ans != null) {
				VarVal vv = new VarVal();
				vv.setKey(getModel().getResultKey());
				vv.setVal(ans);
				vv.setLv(getModel().getResultLv());
				set.add(vv);
			}
		}
		return set;
	}

	private Object executeByType(Value function, DataType dt) {
		switch (dt) {
		case string:
			return function.asString();
		case Boolean:
			return function.asBoolean();
		case number:
			return function.asDouble();
		case array:
			return function.as(List.class);
		case object:
			return function.as(Map.class);
		case NotSupport:
		case NULL:
			function.asByte();
			break;
		}
		return null;
	}

}
