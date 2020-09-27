package net.bzk.flow.run.flow;

import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import net.bzk.flow.model.Action;
import net.bzk.flow.model.Box;
import net.bzk.flow.model.Link;
import net.bzk.flow.model.var.BaseVar;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.action.ActionCall;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.service.RunVarService;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
@Scope("prototype")
public class BoxRuner implements Runnable {

	@Inject
	private ApplicationContext context;
	@Inject
	private RunVarService runVarService;
	@Getter
	private Future<?> task;
	@Getter
	private Box model;
	@Getter
	private String uid;
	@Getter
	private BaseVar vars = new BaseVar();
	@Getter
	private Bundle bundle;

	@Data
	@Builder
	public static class Bundle {
		private String runFlowUid;
		private String flowUid;
		private FlowRuner flowRuner;
		private ThreadPoolExecutor threadPool;

	}

	public BoxRuner init(Bundle b, Box m) {
		model = m;
		bundle = b;
		uid = RandomStringUtils.randomAlphanumeric(32);
		task = bundle.threadPool.submit(this);
		return this;
	}

	private Uids genUids() {
		Uids ans = new Uids();
		ans.setBoxUid(model.getUid());
		ans.setRunBoxUid(uid);
		ans.setFlowUid(bundle.flowUid);
		ans.setRunFlowUid(bundle.runFlowUid);
		return ans;
	}

	@Override
	public synchronized void run() {

		for (String uid : model.getTaskSort()) {
			Optional<Action> ao = findAction(uid);
			if (ao.isPresent()) {
				Action a = ao.get();
				VarValB vvs = callAction(a);
				runVarService.putVarVals(vvs.call.getUids(), vvs.set);
				continue;
			}
			Optional<Link> lo = findLink(uid);
			if (lo.isPresent()) {
				Link l = lo.get();
				if (runLink(l)) {
					return;
				}
				continue;
			}
			throw new BzkRuntimeException("not find any task uid:" + uid);

		}

	}

	private boolean runLink(Link l) {
		if (Conditioner.initConditioner(context, l.getCondition()).isTrue()) {
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
		ActionCall naer = context.getBean(a.getClazz(), ActionCall.class);
		naer.initBase(genUids(), a);
		Callable<VarValSet> cb = naer;
		try {
			return new VarValB(naer, cb.call());
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
