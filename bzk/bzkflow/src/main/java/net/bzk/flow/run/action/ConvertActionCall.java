package net.bzk.flow.run.action;

import java.util.HashMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.model.Action.ConvertAction;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service("net.bzk.flow.model.Action$ConvertAction")
@Scope("prototype")
@Slf4j
public class ConvertActionCall extends ActionCall<ConvertAction> {

	public ConvertActionCall() {
		super(ConvertAction.class);
	}

	@Override
	public VarValSet call() throws Exception {
		String code = getModel().getCode();
		Object o = getPolyglotEngine().parseByStringCode(getModel().getPolyglot().toString(),code);
		logUtils.logActionCall( getUids(), o.getClass() +":"+ o);
		Object oo = convert(o);
		var logm = new HashMap<String,Object>();
		logm.put("original", o);
		logm.put("result", oo);
		logUtils.logActionCall( getUids(), JsonUtils.toJson(logm));
		return VarValSet.genSingle(getModel().getOutput().getKey(), getModel().getOutput().getLv(), oo);
	}

	private Object convert(Object ino) {
		switch (getModel().getMethod()) {
		case ToJSONText:
			return JsonUtils.toJson(ino);
		}
		throw new BzkRuntimeException("Not suport this method:" + getModel().getMethod());

	}

}
