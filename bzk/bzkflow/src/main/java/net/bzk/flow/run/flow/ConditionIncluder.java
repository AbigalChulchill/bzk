package net.bzk.flow.run.flow;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Condition.ConditionInclude;

@Service("net.bzk.flow.model.flow.Condition$ConditionInclude")
@Scope("prototype")
public class ConditionIncluder extends Conditioner<ConditionInclude> {

	@Override
	public boolean checkSelf() {
		return initConditioner(context,getModel().getInclude()).isTrue();
	}

}
