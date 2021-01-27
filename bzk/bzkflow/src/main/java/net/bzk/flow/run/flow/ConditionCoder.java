package net.bzk.flow.run.flow;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Condition.ConditionCode;
import net.bzk.flow.model.Condition.ConditionNum;
import net.bzk.flow.run.action.ActionCall;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service("net.bzk.flow.model.Condition$ConditionCode")
@Scope("prototype")
public class ConditionCoder extends Conditioner<ConditionCode> {

	public ConditionCoder() {
		super(ConditionCode.class);
	}

	@Override
	public boolean checkSelf() {
		Object o = ActionCall.callPolyglot(varQueryer, getModel().getPolyglot().toString(), getModel().getCode());
		if (o instanceof Boolean) {
			Boolean b = (Boolean) o;
			return b;
		}
		throw new BzkRuntimeException("not support type:" + o);
	}

}
