package net.bzk.flow.run.flow;

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Condition.ConditionTxt;
import net.bzk.flow.model.Condition.Val;
import net.bzk.flow.run.service.RunVarService;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service("net.bzk.flow.model.flow.Condition$ConditionTxt")
@Scope("prototype")
public class ConditionTxter extends Conditioner<ConditionTxt> {

	@Inject
	private RunVarService varService;

	@Override
	public boolean checkSelf() {
		Val l = getModel().getLeft();
		Val r = getModel().getRight();
		String ls = varService.getByVal(l) + "";
		String rs = varService.getByVal(r) + "";
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
