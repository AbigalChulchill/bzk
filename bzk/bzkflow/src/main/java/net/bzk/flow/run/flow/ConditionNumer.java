package net.bzk.flow.run.flow;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Condition.ConditionNum;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service("net.bzk.flow.model.Condition$ConditionNum")
@Scope("prototype")
public class ConditionNumer extends Conditioner<ConditionNum> {


	public ConditionNumer( ) {
		super(ConditionNum.class);
	}

	@Override
	public boolean checkSelf() {
		double rd =  getPolyglotEngine().parseScriptbleText(getModel().getRight(),Double.class) ;
		double ld = getPolyglotEngine().parseScriptbleText(getModel().getLeft(),Double.class) ;
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


}
