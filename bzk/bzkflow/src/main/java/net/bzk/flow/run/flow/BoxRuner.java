package net.bzk.flow.run.flow;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.Constant;
import net.bzk.flow.model.Action;
import net.bzk.flow.model.Box;
import net.bzk.flow.model.Condition;
import net.bzk.flow.model.ConvertInfra.VarValList;
import net.bzk.flow.model.Link;
import net.bzk.flow.model.RunLog.RunState;
import net.bzk.flow.model.Transition;
import net.bzk.flow.model.var.VarLv.VarKey;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.action.ActionCall;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.service.RunLogService;
import net.bzk.flow.run.service.RunVarService;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
@Scope("prototype")
@Slf4j
public class BoxRuner {

	@Inject
	private ApplicationContext context;
	@Inject
	private RunVarService runVarService;
	@Inject
	private RunLogService logUtils;
	@Getter
	private Box model;
	@Getter
	private String uid;
	@Getter
	@Setter
	private VarMap vars = new VarMap();
	@Getter
	private Bundle bundle;

	@Data
	@Builder
	public static class Bundle {
		private String runFlowUid;
		private String flowUid;
		private FlowRuner flowRuner;
	}

	public BoxRuner init(Bundle b, Box m) {
		model = m;
		bundle = b;
		uid = RandomStringUtils.randomAlphanumeric(Constant.RUN_UID_SIZE);
		vars = m.getVars();
		return this;
	}

	public Uids genUids() {
		Uids ans = new Uids();
		ans.setBoxUid(model.getUid());
		ans.setRunBoxUid(uid);
		ans.setFlowUid(bundle.flowUid);
		ans.setRunFlowUid(bundle.runFlowUid);
		return ans;
	}

	public synchronized void run() {

		logUtils.log(genUids(), RunState.BoxStart, l -> {
		});
		if (model.getWhileJudgment() == null) {
			if (rundownAndLog())
				return;
		} else {
			while (checkConditioner(model.getWhileJudgment())) {
				if (rundownAndLog()) {
					return;
				}
				logUtils.log(genUids(), RunState.WhileLoopBottom);
			}
		}
		if (model.getTransition().isEnd()) {
			endFlow(model.getTransition());
		} else {
			transitBox(model.getTransition());
		}
	}

	private boolean rundownAndLog() {
		logUtils.log(genUids(), RunState.BoxLoop);
		boolean b = rundown();
//		logUtils.log(genUids(), RunState.BoxLoopDone, l -> {
//			l.setMsg("break:" + b);
//		});

		return b;
	}

	// true : break; false : continue
	private boolean rundown() {
		List<String> taskSort = model.getTaskSort();
		for (int i = 0; i < taskSort.size(); i++) {
			String tuid = taskSort.get(i);
			if (runAction(tuid)) {
				continue;
			}
			Link l = findLink(tuid).get();
			if (runLink(l)) {
				return true;
			}
		}
		return false;
	}

	public boolean runAction(String aUid) {
		Optional<Action> ao = findAction(aUid);
		if (!ao.isPresent()) {
			return false;
		}
		Action a = ao.get();
		VarValSet vvs = callAction(a);
		if (vvs != null) {
			runVarService.putVarVals(genUids(), vvs);

			

		}

		logUtils.log(genUids(), RunState.EndAction);

		return true;
	}

	private boolean endFlow(Transition t) {
		if (!t.isEnd())
			return false;
		logUtils.log(genUids(), RunState.EndFlow, l -> l.setMsg(t.getEndTag()));
		bundle.flowRuner.onEnd(t, listEndResult(t));
		return true;
	}

	private List<VarVal> listEndResult(Transition t) {
		return t.getEndResultKeys().stream().map(this::getVarVal).collect(Collectors.toList());
	}

	private VarVal getVarVal(VarKey k) {
		VarVal ans = new VarVal();
		Object o = runVarService.findValVal(genUids(), k.getLv(), k.getKey()).get();
		ans.setKey(k.getKey());
		ans.setLv(k.getLv());
		ans.setVal(o);
		return ans;
	}

	private boolean runLink(Link l) {
		if (checkConditioner(l.getCondition())) {
			if (!endFlow(l.getTransition())) {
				transitBox(l.getTransition());
			}
			return true;
		}
		return false;
	}

	private void transitBox(Transition t) {
		logUtils.log(genUids(), RunState.LinkTo);
		var rl = listEndResult(t);
		bundle.flowRuner.runBoxByUid(t.getToBox(), rl);
	}

	private boolean checkConditioner(Condition c) {
		var cer = Conditioner.initConditioner(context, genUids(), c);
		return cer.isTrue();
	}

	public Optional<Action> findAction(String uid) {
		return model.getActions().stream().filter(a -> StringUtils.equals(uid, a.getUid())).findFirst();
	}

	public Optional<Link> findLink(String uid) {
		return model.getLinks().stream().filter(l -> StringUtils.equals(uid, l.getUid())).findFirst();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private VarValSet callAction(Action a) {
		try {
			logUtils.log(genUids(), RunState.StartAction);
			ActionCall naer = context.getBean(a.getClazz(), ActionCall.class);
			naer.initBase(genUids(), a);
			Callable<VarValSet> cb = naer;
			VarValSet ans = cb.call();
			logUtils.log(naer.getUids(), RunState.ActionResult, l -> l.setVarVals(VarValList.gen(ans.list())));
			return ans;
		} catch (Exception e) {
			logUtils.log(genUids(), RunState.ActionCallFail,l->{
				l.setFailed(true);
				l.setMsg(e.getMessage());
				l.setException(ExceptionUtils.getStackTrace(e));
				l.setExceptionClazz(e.getClass().toGenericString());
			});
			if (a.isTryErrorble()) {
				return VarValSet.genError(a, e);
			}
			throw new BzkRuntimeException(e);
		}
	}

//	public static class VarValB {
//		public ActionCall call;
//		public VarValSet set;
//
//		public VarValB(ActionCall call, VarValSet set) {
//			this.call = call;
//			this.set = set;
//		}
//
//	}

}
