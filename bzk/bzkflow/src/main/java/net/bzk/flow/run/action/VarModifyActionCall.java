package net.bzk.flow.run.action;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.apache.commons.text.StringEscapeUtils;
import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.model.Action.KVPair;
import net.bzk.flow.model.Action.VarModifyAction;
import net.bzk.flow.model.var.VarLv;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.infrastructure.JsonUtils;

@Service("net.bzk.flow.model.Action$VarModifyAction")
@Scope("prototype")
@Slf4j
public class VarModifyActionCall extends ActionCall<VarModifyAction> {

	public VarModifyActionCall() {
		super(VarModifyAction.class);
	}

	@Override
	public VarValSet call() throws Exception {
		List<KVPair> kvs = getModel().getFlatData();
		List<VarVal> vlist = new ArrayList<>();
		for (var kp : kvs) {
			vlist.add(genVarVal(kp));
		}
		return null;
	}

	private VarVal genVarVal(KVPair e) {
		var kinfo = VarLv.checkLvByPrefix(e.getKey());
		VarVal ans = new VarVal();
		ans.setKey(kinfo.getKey());
		ans.setLv(kinfo.getLv());
		String code = e.getVal();
		Object o = parseByStringCode(log,getModel().getPolyglot().toString(),code);
		logUtils.logActionCall(log, getUids(), o.getClass() +":"+ o);
		ans.setVal(o);
		varService.putVarVal(getUids(), ans);
		return ans;
	}

//	private Object parseByStringCode(String ifCode) {
//		logUtils.logActionCall(log, getUids(), ifCode);
//		Object o= JsonUtils.stringToValue(ifCode);
//		logUtils.logActionCall(log, getUids(), o.getClass()+" "+o);
//		if(o instanceof String) {
//			try {
//				Object ans= callPolyglot(getModel().getPolyglot().toString(), o.toString());
//				logUtils.logActionCall(log, getUids(),  "callPolyglot "+ans.getClass()+" "+ans);
//				return ans;
//			} catch (Exception e) {
//				logUtils.logActionCallWarn(log, getUids(), e.getMessage());
//			}
//		}
//		return o;
//	}

}
