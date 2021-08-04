package net.bzk.flow.run.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import net.bzk.flow.api.dto.FlowPoolInfo;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.Flow.ActionFindInfo;
import net.bzk.flow.run.dao.JobsDao;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.dao.RunFlowDao;
import net.bzk.flow.run.dao.RunFlowPoolDao;
import net.bzk.flow.run.flow.BoxRuner;
import net.bzk.flow.run.flow.FlowRuner;
import net.bzk.flow.run.flow.FlowRuner.RunInfo;
import net.bzk.flow.run.service.JobsService.InitFlowEvent;

@Service
public class RunFlowService implements ApplicationListener<InitFlowEvent> {

	@Inject
	private RunFlowPoolDao runPoolDao;
	@Inject
	private RunFlowDao dao;
	@Inject
	private RunBoxDao boxDao;
	@Inject
	private JobsDao jobsDao;

	@Value("${init.data.autoegister}")
	private boolean autoegister;

	public void register(Flow f) {
		boolean b = runPoolDao.create(f);
		runPoolDao.launch(f.getUid());
	}

	public void registerDepends(String uid) {
		var sfs = jobsDao.listDepends(uid);
		List<Flow> fs = sfs.stream().map(sf -> sf.getModel()).collect(Collectors.toList());
		fs.forEach(this::register);
	}

	public RunInfo test(String entryUid, List<Flow> fs) {
		Flow ef = fs.stream().filter(f -> StringUtils.equals(f.getUid(), entryUid)).findFirst().get();
		fs.remove(ef);
		fs.forEach(f -> {
			runPoolDao.create(f);
		});
		var fr = dao.create(ef, r -> {
		});
		fr.run();
		fs.forEach(f -> {
			runPoolDao.forceRemove(f.getUid());
		});
		return fr.getInfo();
	}

	public FlowRuner runManual(String uid) {
		var p = runPoolDao.getPool(uid);
		return p.createAndStart();
	}

	public List<FlowPoolInfo> listFlowPoolInfo() {
		return runPoolDao.listPools().stream().map(p -> new FlowPoolInfo(p.getModel(), p.listRunInfos(false)))
				.collect(Collectors.toList());
	}

	public List<RunInfo> listArchiveRunInfo(String uid) {
		var p = runPoolDao.getPool(uid);
		return p.listArchiveRunInfo();
	}
	
	public FlowPoolInfo getFlowPoolInfo(String uid) {
		var p = runPoolDao.getPool(uid);
		return new FlowPoolInfo(p.getModel(), p.listRunInfos(true));
	}

	public void forceRemove(String fuid) {
		runPoolDao.forceRemove(fuid);
	}

	public void testAction(String flowUid, String actionUid, long delDelay) throws InterruptedException {
		Flow flow = jobsDao.findById(flowUid).get().getModel();
		String uid = RandomStringUtils.randomAlphanumeric(64);
		ActionFindInfo afi = flow.getAction(actionUid);
		flow.setUid(uid);
		flow.setVars(afi.getAction().getDevFlowVars());
		FlowRuner fr = dao.create(flow, r -> {
		});
		BoxRuner br = fr.createBoxByUid(afi.getBox().getUid());
		br.setVars(afi.getAction().getDevBoxVars());
		br.runAction(actionUid);
		if (delDelay > 0) {
			Thread.sleep(delDelay);
		}
		boxDao.remove(br);
		dao.remove(fr.getInfo().getUid());
	}

	@Override
	public void onApplicationEvent(InitFlowEvent event) {
		if (!autoegister)
			return;
		System.out.println("InitFlowEvent :" + event);
		var fs = event.getFlows();
		fs.stream().filter(f -> f.getEntry().isAutoRegister()).forEach(af -> registerDepends(af.getUid()));

	}
}
