package net.bzk.flow.run.flow;

import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.Getter;
import net.bzk.flow.model.Box;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.var.BaseVar;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.dao.RunFlowPool;
import net.bzk.infrastructure.CommUtils;

@Service
@Scope("prototype")
public class FlowRuner implements Runnable {

	@Inject
	private RunBoxDao runBoxDao;
	private RunFlowPool pool;
	@Getter
	private String uid;
	private Flow model;
	@Getter
	private Future<?> task;
	@Getter
	private BaseVar vars;
	private BoxRuner currentBoxRuner;

	public FlowRuner init(RunFlowPool p, Flow f) {
		model = f;
		pool = p;
		uid = RandomStringUtils.randomAlphanumeric(32);

		vars = CommUtils.toByJson(model.getVars(), BaseVar.class);
		return this;
	}

	public void start(ThreadPoolExecutor tp) {
		task = tp.submit(this);

	}

	@Override
	public void run() {
		Box ob = model.findEntryBox();
		currentBoxRuner = runBoxDao.create(genBoxBundle(), ob);
	}

	private BoxRuner.Bundle genBoxBundle() {
		BoxRuner.Bundle ans = BoxRuner.Bundle.builder()
		.flowUid(model.getUid())
		.runFlowUid(uid)
		.flowRuner(this)
		.threadPool(pool.getThreadPool()).build();
		return ans;

	}

	public void runBoxByUid(String boxUid) {
		Box bm = model.getBoxs().stream().filter(b -> StringUtils.equals(boxUid, b.getUid())).findFirst().get();
		currentBoxRuner = runBoxDao.create(genBoxBundle(), bm);
	}

	public void cancelCurrentBox() {
		currentBoxRuner.getTask().cancel(false);
	}

}
