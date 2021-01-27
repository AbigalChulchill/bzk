package net.bzk.flow.run.flow;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.Constant;
import net.bzk.flow.model.Box;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.Link;
import net.bzk.flow.model.Transition;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.entry.Entryer;
import net.bzk.infrastructure.JsonUtils;

@Service
@Scope("prototype")
@Slf4j
public class FlowRuner implements Runnable {

	public static enum State {
		Pedding, Running, Done, Fail
	}

	@Inject
	private RunBoxDao runBoxDao;
	@Getter
	private RunInfo info = new RunInfo();
	@Getter
	private Flow model;
	@Getter
	private Future<?> task;
	@Getter
	private VarMap vars;
	private BoxRuner currentBoxRuner;
	private Consumer<FlowRuner> callback;

	public FlowRuner init(Flow f, Consumer<FlowRuner> c) {
		callback = c;
		model = f;
		info.uid = RandomStringUtils.randomAlphanumeric(Constant.RUN_UID_SIZE);
		vars = JsonUtils.toByJson(model.getVars(), VarMap.class);
		return this;
	}

	public void start(ThreadPoolExecutor tp) {
		task = tp.submit(this);
	}

	@Override
	public void run() {
		try {
			info.state = State.Running;
			Box ob = model.findEntryBox();
			currentBoxRuner = runBoxDao.create(genBoxBundle(), ob);
			currentBoxRuner.run();
		} catch (Exception e) {
			info.state = State.Fail;
		}

	}

	private BoxRuner.Bundle genBoxBundle() {
		BoxRuner.Bundle ans = BoxRuner.Bundle.builder().flowUid(model.getUid()).runFlowUid(info.uid).flowRuner(this)
				.build();
		return ans;

	}

	public void runBoxByUid(String boxUid, List<VarVal> rl) {
		if (currentBoxRuner != null) {
			runBoxDao.remove(currentBoxRuner);
		}
		currentBoxRuner = createBoxByUid(boxUid);
		rl.forEach(r-> currentBoxRuner.getVars().putByPath(r.getKey(), r.getVal()));
		currentBoxRuner.run();
	}

	public BoxRuner createBoxByUid(String boxUid) {
		Box bm = model.getBoxs().stream().filter(b -> StringUtils.equals(boxUid, b.getUid())).findFirst().get();
		return runBoxDao.create(genBoxBundle(), bm);

	}

	public void onEnd(Transition l, List<VarVal> re) {
		info.state = State.Done;
		info.transition = l;
		info.endResult = re;
		runBoxDao.remove(currentBoxRuner);
		callback.accept(this);
	}
	
	

	@Data
	public static class RunInfo {
		private String uid;
		private State state = State.Pedding;;
		private Transition transition;
		private List<VarVal> endResult;

	}

}
