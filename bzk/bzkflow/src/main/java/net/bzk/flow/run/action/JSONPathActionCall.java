package net.bzk.flow.run.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.model.Action.JSONPathAction;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.infrastructure.JsonUtils;

@Service("net.bzk.flow.model.Action$JSONPathAction")
@Scope("prototype")
@Slf4j
public class JSONPathActionCall extends ActionCall<JSONPathAction> {
	
	public JSONPathActionCall() {
		super(JSONPathAction.class);
	}

	@Override
	public VarValSet call() throws Exception {
		Object o= varService.findValVal(getUids(), getModel().getSource().getLv(),  getModel().getSource().getKey()).get();
		Object ans = JsonUtils.findByJsonPath(o, getModel().getSyntax());
		return VarValSet.genSingle(getModel().getTarget().getKey(), getModel().getTarget().getLv(), ans );
	}

}
