package net.bzk.flow.run.flow;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;

import lombok.Getter;
import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.model.Condition;
import net.bzk.flow.model.Condition.ConKind;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.service.FastVarQueryer;
import net.bzk.infrastructure.ex.BzkRuntimeException;

public abstract class Conditioner<T extends Condition> {
	@Getter
	private Class<T> modelClazz;
	@Getter
	private T model;
	@Inject
	protected ApplicationContext context;
	@Getter
	private Uids uids;
	@Inject
	protected FastVarQueryer varQueryer;

	public Conditioner(Class<T> c) {
		modelClazz = c;
	}
	
	protected Conditioner init(Uids u, T c) {
		uids = u;
		model = c;
		varQueryer.init(uids);
		model = BzkFlowUtils.replaceModel(varQueryer, model, modelClazz);
		return this;
	}

	public abstract boolean checkSelf();

	public boolean isTrue() {
		ConKind ck = model.getKind();
		if (ck == ConKind.NONE)
			return checkSelf();
		Conditioner ncn = initConditioner(context, uids, model.getNext());
		if (ck == ConKind.AND)
			return checkSelf() && ncn.isTrue();
		if (ck == ConKind.OR)
			return checkSelf() || ncn.isTrue();
		throw new BzkRuntimeException("not support this kind :" + ck);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Conditioner initConditioner(ApplicationContext context, Uids u, Condition m) {
		return context.getBean(m.getClazz(), Conditioner.class).init(u, m);
	}

}
