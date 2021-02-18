package net.bzk.flow.run.action;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.api.dto.VarKeyReflect;
import net.bzk.flow.model.Action.KVPair;
import net.bzk.flow.model.Action.SubFlowAction;
import net.bzk.flow.model.var.VarLv;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.dao.RunFlowPoolDao;

@Service("net.bzk.flow.model.Action$SubFlowAction")
@Scope("prototype")
@Slf4j
public class SubFlowActionCall extends ActionCall<SubFlowAction> {

	@Inject
	private RunFlowPoolDao flowPoolDao;

	public SubFlowActionCall() {
		super(SubFlowAction.class);
	}

	@Override
	public VarValSet call() throws Exception {

		var frp = flowPoolDao.getPool(getModel().getFlowUid());
		var fr = frp.create();

		List<KVPair> kvs = getModel().getInputData();
		for (var kp : kvs) {
			Object rv = parseByStringCode( getModel().getPolyglot().toString(), kp.getVal());
			var kinfo = VarLv.checkLvByPrefix(kp.getKey());
			fr.getVars().put(kinfo.getKey(), rv);
		}

		fr.run();
		List<VarVal> ers = fr.getInfo().getEndResult();
		VarValSet ans = new VarValSet();
		List<VarKeyReflect> omap = getModel().getOutputReflects();
		for (VarVal vv : ers) {
			Optional<VarKeyReflect> vkro = VarKeyReflect.findBySrcKey(omap, vv.getKey());
			if(vkro.isEmpty()) continue;
			VarKeyReflect vkr = vkro.get();
			VarVal nvv = new VarVal();
			nvv.setKey(vkr.getToKey().getKey());
			nvv.setLv(vkr.getToKey().getLv());
			nvv.setVal(vv.getVal());
			ans.add(nvv);
		}
		return ans;
	}

}
