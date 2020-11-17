package net.bzk.flow.run.flow;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
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
import net.bzk.flow.model.Link;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.model.var.VarMap.VarProvider;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.action.ActionCall;
import net.bzk.flow.run.action.NodejsActionCall;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.service.RunVarService;
import net.bzk.flow.utils.LogUtils;
import net.bzk.flow.utils.LogUtils.BoxRunLog;
import net.bzk.flow.utils.LogUtils.ActionRunLog;
import net.bzk.flow.utils.LogUtils.BoxRunState;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
@Scope("prototype")
@Slf4j
public class BoxRuner implements VarProvider {

	@Inject
	private ApplicationContext context;
	@Inject
	private RunVarService runVarService;
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
		LogUtils.logBoxRun(log, new BoxRunLog(this).state(BoxRunState.BoxStart));
		List<String> taskSort = model.getTaskSort();
		for (int i = 0; i < taskSort.size(); i++) {
			String tuid = taskSort.get(i);
			if (runAction(tuid)) {
				continue;
			}
			Link l = findLink(tuid).get();
			if (endFlow(l)) {
				return;
			}
			if (runLink(l)) {
				return;
			}
			continue;
		}

	}


	public boolean runAction(String aUid) {
		Optional<Action> ao = findAction(aUid);
		if (!ao.isPresent()) {
			return false;
		}
		Action a = ao.get();
		VarValB vvs = callAction(a);
		runVarService.putVarVals(vvs.call.getUids(), vvs.set);
		return true;
	}

	private boolean endFlow(Link l) {
		if (!l.isEnd())
			return false;
		LogUtils.logBoxRun(log, new BoxRunLog(this).state(BoxRunState.EndFlow));
		bundle.flowRuner.onEnd(l);
		return true;
	}

	private boolean runLink(Link l) {
		if (l.isDirected() || Conditioner.initConditioner(context, l.getCondition()).isTrue()) {
			LogUtils.logBoxRun(log, new BoxRunLog(this).state(BoxRunState.LinkTo));
			bundle.flowRuner.runBoxByUid(l.getToBox());
			return true;
		}
		return false;
	}

	public Optional<Action> findAction(String uid) {
		return model.getActions().stream().filter(a -> StringUtils.equals(uid, a.getUid())).findFirst();
	}

	public Optional<Link> findLink(String uid) {
		return model.getLinks().stream().filter(l -> StringUtils.equals(uid, l.getUid())).findFirst();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private VarValB callAction(Action a) {
		try {
			LogUtils.logBoxRun(log, new ActionRunLog(this, a).state(BoxRunState.StartAction));
			ActionCall naer = context.getBean(a.getClazz(), ActionCall.class);
			naer.initBase(genUids(), a);
			Callable<VarValSet> cb = naer;
			VarValB ans = new VarValB(naer, cb.call());
			LogUtils.logBoxRun(log, new ActionRunLog(this, a).varVals(ans.set.list()).state(BoxRunState.EndAction));
			return ans;
		} catch (Exception e) {
			throw new BzkRuntimeException(e);
		}
	}

	public static class VarValB {
		public ActionCall call;
		public VarValSet set;

		public VarValB(ActionCall call, VarValSet set) {
			this.call = call;
			this.set = set;
		}

	}

}
