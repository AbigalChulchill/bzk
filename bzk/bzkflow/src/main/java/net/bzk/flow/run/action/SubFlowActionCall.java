package net.bzk.flow.run.action;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import net.bzk.flow.Constant;
import net.bzk.flow.enums.Enums;
import net.bzk.flow.run.flow.FlowRuner;
import net.bzk.infrastructure.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.api.dto.VarKeyReflect;
import net.bzk.flow.model.Action.KVPair;
import net.bzk.flow.model.Action.SubFlowAction;
import net.bzk.flow.enums.VarLv;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.dao.RunFlowPoolDao;
import net.bzk.flow.run.flow.FlowRuner.State;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service("net.bzk.flow.model.Action$SubFlowAction")
@Scope("prototype")
@Slf4j
public class SubFlowActionCall extends ActionCall<SubFlowAction> {

    @Inject
    @Qualifier("threadPoolTaskExecutor")
    private ExecutorService executorService;

    @Inject
    private RunFlowPoolDao flowPoolDao;

    public SubFlowActionCall() {
        super(SubFlowAction.class);
    }


    @Override
    public VarValSet call() throws Exception {
        var frp = flowPoolDao.getPool(getModel().getFlowUid());
        var fr = frp.create();
        injectParams(fr);
        return runRunner(fr);
    }

    private VarValSet runRunner(FlowRuner fr) {
        logUtils.log(getUids(), Enums.RunState.ActionCall, r -> {
            r.setRefRunFlowUid(fr.getInfo().getUid());
            r.setMsg(JsonUtils.toJson(fr.getModel()));
        });
        if (getModel().isAsynced()) {
            executorService.execute(fr::run);
            return new VarValSet();
        }
        fr.run();
        String endTag = getEndTag(fr.getInfo());
        if (fr.getInfo().getState() == State.Fail) {
            logUtils.logActionCall(getUids(), "getState is Fail");
            throw new BzkRuntimeException(endTag);
        }
        return genVarSet(fr);

    }

    private void injectParams(FlowRuner fr) {
        List<KVPair> kvs = getModel().getInputData();
        for (var kp : kvs) {
            Object rv = getPolyglotEngine().parseScriptbleText(kp.getVal(), Object.class);
            var kinfo = VarLv.checkLvByPrefix(kp.getKey());
            fr.getVars().put(kinfo.getKey(), rv);
        }
    }

    private VarValSet genVarSet(FlowRuner fr) {
        Map<String,Object> ers = fr.getInfo().getEndResult();
        VarValSet ans = new VarValSet();
        List<VarKeyReflect> omap = getModel().getOutputReflects();
        for (var vv : ers.entrySet()) {
            Optional<VarKeyReflect> vkro = VarKeyReflect.findBySrcKey(omap, vv.getKey());
            if (vkro.isEmpty())
                continue;
            VarKeyReflect vkr = vkro.get();
            addVarVal(ans, vkr.getToKey().getLv(), vkr.getToKey().getKey(), vv.getValue());
        }
        addVarVal(ans, VarLv.run_box, Constant.subStateKey(fr.getModel().getName()), fr.getInfo().getState());
        addVarVal(ans, VarLv.run_box, Constant.subTagKey(fr.getModel().getName()), getEndTag(fr.getInfo()));
        return ans;
    }

    private void addVarVal(VarValSet ans, VarLv l, String k, Object v) {
        VarVal nvv = new VarVal();
        nvv.setKey(k);
        nvv.setLv(l);
        nvv.setVal(v);
        ans.add(nvv);
    }

    private String getEndTag(FlowRuner.RunInfo ri) {
        if (ri.getTransition() == null) return StringUtils.EMPTY;
        if (ri.getTransition().getEndTag() == null) return StringUtils.EMPTY;
        return ri.getTransition().getEndTag();
    }

}
