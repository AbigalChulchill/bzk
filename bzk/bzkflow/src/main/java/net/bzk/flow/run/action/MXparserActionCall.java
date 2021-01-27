package net.bzk.flow.run.action;

import java.util.List;

import javax.inject.Inject;

import org.mariuszgromada.math.mxparser.Expression;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.model.Action.MXparserAction;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.service.FastVarQueryer;
import net.bzk.infrastructure.PlaceholderUtils;
import net.bzk.infrastructure.StrPlacer;

@Service("net.bzk.flow.model.Action$MXparserAction")
@Scope("prototype")
@Slf4j
public class MXparserActionCall extends ActionCall<MXparserAction> {



	@Inject
	private FastVarQueryer queryer;
	
	public MXparserActionCall() {
		super(MXparserAction.class);
	}

	@Override
	public VarValSet call() throws Exception {
		queryer.init(getUids());
		String code = getModel().getCode();
		List<String> vs = PlaceholderUtils.listStringSubstitutorKeys(code);
		StrPlacer sp = PlaceholderUtils.build(code);
		vs.forEach(v -> sp.place(v, queryer.g(v) + ""));
		Expression e = new Expression(sp.replace());
		double ans = e.calculate();
		return VarValSet.genSingle(getModel().getResultKey(), getModel().getResultLv(), ans);
	}

}
