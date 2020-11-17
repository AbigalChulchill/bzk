package net.bzk.flow.run.flow;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.Constant;
import net.bzk.flow.api.dto.RegisteredFlow.RunInfo;
import net.bzk.flow.model.Box;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.Link;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.model.var.VarMap.VarProvider;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.dao.RunFlowPool;
import net.bzk.infrastructure.JsonUtils;

@Service
@Scope("prototype")
@Slf4j
public class FlowRuner implements Runnable, VarProvider {

	public static enum State {
		Pedding, Running, Done, Fail
	}

	@Inject
	private RunBoxDao runBoxDao;
	@Getter
	private String uid;
	private Flow model;
	@Getter
	private Future<?> task;
	@Getter
	private VarMap vars;
	private BoxRuner currentBoxRuner;
	private State state = State.Pedding;
	private Link endLink;
	private Consumer<FlowRuner> callback;

	public FlowRuner init(Flow f, Consumer<FlowRuner> c) {
		callback = c;
		model = f;
		uid = RandomStringUtils.randomAlphanumeric(Constant.RUN_UID_SIZE);
		vars = JsonUtils.toByJson(model.getVars(), VarMap.class);
		return this;
	}

	public void start(ThreadPoolExecutor tp) {
		task = tp.submit(this);
	}

	@Override
	public void run() {
		try {
			state = State.Running;
			Box ob = model.findEntryBox();
			currentBoxRuner = runBoxDao.create(genBoxBundle(), ob);
			currentBoxRuner.run();
		} catch (Exception e) {
			state = State.Fail;
		}

	}

	private BoxRuner.Bundle genBoxBundle() {
		BoxRuner.Bundle ans = BoxRuner.Bundle.builder().flowUid(model.getUid()).runFlowUid(uid).flowRuner(this).build();
		return ans;

	}

	public void runBoxByUid(String boxUid) {
		if (currentBoxRuner != null) {
			runBoxDao.remove(currentBoxRuner);
		}
		currentBoxRuner = createBoxByUid(boxUid);
		currentBoxRuner.run();
	}

	public BoxRuner createBoxByUid(String boxUid) {
		Box bm = model.getBoxs().stream().filter(b -> StringUtils.equals(boxUid, b.getUid())).findFirst().get();
		return runBoxDao.create(genBoxBundle(), bm);

	}

	public void onEnd(Link l) {
		state = State.Done;
		endLink = l;
		runBoxDao.remove(currentBoxRuner);
		callback.accept(this);
	}

	public RunInfo getRunInfo() {
		return new RunInfo(getUid(), state, endLink.getEndTag(), endLink.getUid());
	}

}
