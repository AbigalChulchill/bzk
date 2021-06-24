package net.bzk.flow.run.flow;

import javax.inject.Inject;

import net.bzk.infrastructure.JsonUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.ApplicationContext;

import lombok.Getter;
import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.infra.PolyglotEngine;
import net.bzk.flow.model.Condition;
import net.bzk.flow.model.Condition.ConKind;
import net.bzk.flow.model.RunLog.RunState;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.service.FastVarQueryer;
import net.bzk.flow.run.service.RunLogService;
import net.bzk.infrastructure.ex.BzkRuntimeException;

import java.util.function.Function;

public abstract class Conditioner<T extends Condition> {
    @Getter
    private Class<T> modelClazz;
    @Getter
    private T model;
    @Inject
    protected ApplicationContext context;
    @Getter
    private Uids uids;
    @Getter
    private PolyglotEngine polyglotEngine;
    @Inject
    private FastVarQueryer varQueryer;
    @Inject
    protected RunLogService logUtils;

    protected Function<String, Object> replaceFunc;

    public Conditioner(Class<T> c) {
        modelClazz = c;
    }

    protected Conditioner init(Uids u, T c) {
        uids = u;
        varQueryer.init(u);
        var pe = PolyglotEngine.FlowPolyglotEngine.builder().logUtils(logUtils).varQueryer(varQueryer).build();
        return init(pe, varQueryer, c);
    }

    protected Conditioner init(PolyglotEngine pe, Function<String, Object> rf, T c) {
        model = c;
        polyglotEngine = pe;
        replaceFunc = rf;
        model = replaceModel(getModel());
        return this;
    }

    private T replaceModel(Condition oc) {
        try {
            String aJson = JsonUtils.toJson(oc);
            var rp = BzkFlowUtils.replaceTextNotNull(replaceFunc, aJson);
            Condition c = BzkFlowUtils.getFlowJsonMapper().readValue(rp, Condition.class);
            return (T) c;
        } catch (Exception e) {
            throw new BzkRuntimeException(e);
        }
    }

    public abstract boolean checkSelf();

    public boolean isTrue() {
        ConKind ck = model.getKind();
        try {
            if (ck == ConKind.NONE)
                return checkSelf();
            Conditioner ncn = initConditioner(context, polyglotEngine, replaceFunc, model.getNext());
            if (ck == ConKind.AND)
                return checkSelf() && ncn.isTrue();
            if (ck == ConKind.OR)
                return checkSelf() || ncn.isTrue();
            throw new BzkRuntimeException("not support this kind :" + ck);
        } catch (Exception e) {
            logUtils.log(uids, RunState.ConditionFail, l -> {
                l.setFailed(true);
                l.setMsg(e.getMessage());
                l.setException(ExceptionUtils.getStackTrace(e));
                l.setExceptionClazz(e.getClass().toGenericString());
            });
            throw e;
        }

    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Conditioner initConditioner(ApplicationContext context, Uids u, Condition m) {
        return context.getBean(m.getClazz(), Conditioner.class).init(u, m);
    }

    public static Conditioner initConditioner(ApplicationContext context, PolyglotEngine pe, Function<String, Object> rf, Condition m) {
        return context.getBean(m.getClazz(), Conditioner.class).init(pe, rf, m);
    }

}
