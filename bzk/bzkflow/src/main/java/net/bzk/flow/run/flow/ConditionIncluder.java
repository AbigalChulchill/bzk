package net.bzk.flow.run.flow;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Condition.ConditionInclude;

@Service("net.bzk.flow.model.Condition$ConditionInclude")
@Scope("prototype")
public class ConditionIncluder extends Conditioner<ConditionInclude> {

	public ConditionIncluder() {
		super(ConditionInclude.class);
	}

	@Override
	public boolean checkSelf() {
		return initConditioner(context,getUids(),getModel().getInclude()).isTrue();
	}

}
