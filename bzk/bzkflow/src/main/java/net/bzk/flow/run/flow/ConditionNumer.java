package net.bzk.flow.run.flow;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Condition.ConditionNum;
import net.bzk.flow.model.Condition.Val;
import net.bzk.flow.run.service.RunVarService;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service("net.bzk.flow.model.flow.Condition$ConditionNum")
@Scope("prototype")
public class ConditionNumer extends Conditioner<ConditionNum> {

	@Inject
	private RunVarService varService;

	@Override
	public boolean checkSelf() {
		Val r = getModel().getRight();
		Val l = getModel().getLeft();
		double rd = parse(r);
		double ld = parse(l);
		switch (getModel().getType()) {
		case equal:
			return ld == rd;
		case greater:
			return ld > rd;
		case greater_equal:
			return ld >= rd;
		case lessthan:
			return ld < rd;
		case lessthan_equal:
			return ld <= rd;
		}
		throw new BzkRuntimeException("not support type:" + getModel().getType());
	}

	private double parse(Val v) {
		Object vs = varService.getByVal(v);
		return Double.parseDouble(vs.toString());
	}

}
