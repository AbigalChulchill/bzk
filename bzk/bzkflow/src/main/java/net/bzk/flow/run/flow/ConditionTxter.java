package net.bzk.flow.run.flow;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Condition.ConditionTxt;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service("net.bzk.flow.model.Condition$ConditionTxt")
@Scope("prototype")
public class ConditionTxter extends Conditioner<ConditionTxt> {



	public ConditionTxter() {
		super(ConditionTxt.class);
	}

	@Override
	public boolean checkSelf() {

		String ls = getModel().left();
		String rs = getModel().right();
		switch (getModel().getType()) {
		case equal:
			return StringUtils.equals(ls, rs);
		case contains:
			return StringUtils.contains(ls, rs);
		case endsWith:
			return StringUtils.endsWith(ls, rs);
		case startsWith:
			return StringUtils.startsWith(ls, rs);

		}
		throw new BzkRuntimeException("not support type:" + getModel().getType());
	}

}
