package net.bzk.flow.run.flow;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;

import lombok.Getter;
import net.bzk.flow.model.Condition;
import net.bzk.flow.model.Condition.ConKind;
import net.bzk.infrastructure.ex.BzkRuntimeException;

public abstract class Conditioner<T extends Condition> {

	@Getter
	private T model;
	@Inject
	protected ApplicationContext context;

	protected Conditioner init(T c) {
		model = c;
		return this;
	}

	public abstract boolean checkSelf();

	public boolean isTrue() {
		ConKind ck = model.getKind();
		if (ck == ConKind.NONE)
			return checkSelf();
		Conditioner ncn = initConditioner(context,model.getNext());
		if (ck == ConKind.AND)
			return checkSelf() && ncn.isTrue();
		if (ck == ConKind.OR)
			return checkSelf() || ncn.isTrue();
		throw new BzkRuntimeException("not support this kind :" + ck);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Conditioner initConditioner(ApplicationContext context,Condition m) {
		return  context.getBean(m.getClazz(), Conditioner.class).init(m);
	}

}
