package net.bzk.flow.run.action;

import java.time.Duration;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.model.Action.WaitAction;
import net.bzk.flow.model.var.VarValSet;

@Service("net.bzk.flow.model.Action$WaitAction")
@Scope("prototype")
@Slf4j
public class WaitActionCall extends ActionCall<WaitAction> {

	public WaitActionCall() {
		super(WaitAction.class);
	}

	@Override
	public VarValSet call() throws Exception {
		long ms= Duration.of(getModel().getStep(), getModel().getUnit()).toMillis();
		logUtils.logActionCall(log, getUids(), "sleep:"+ ms);
		Thread.sleep(ms);
		return null;
	}


}
