package net.bzk.flow.run.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import net.bzk.flow.api.dto.ActionDebugData;
import net.bzk.flow.api.dto.FlowPoolInfo;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.Flow.ActionFindInfo;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.dao.RunFlowDao;
import net.bzk.flow.run.dao.RunFlowPoolDao;
import net.bzk.flow.run.flow.BoxRuner;
import net.bzk.flow.run.flow.FlowRuner;
import net.bzk.flow.run.flow.FlowRuner.RunInfo;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
public class RunFlowService {

	@Inject
	private RunFlowPoolDao runPoolDao;
	@Inject
	private RunFlowDao dao;
	@Inject
	private RunBoxDao boxDao;

	public void register(Flow f) {
		boolean b = runPoolDao.create(f);
		runPoolDao.launch(f.getUid());
		if (!b)
			throw new BzkRuntimeException("exist :" + f);
	}

	public RunInfo test(String entryUid, List<Flow> fs) {
		Flow ef = fs.stream().filter(f -> StringUtils.equals(f.getUid(), entryUid)).findFirst().get();
		fs.remove(ef);
		fs.forEach(f -> {
			runPoolDao.create(f);
		});
		var fr = dao.create(ef,r-> {});
		fr.run();
		fs.forEach(f -> {
			runPoolDao.forceRemove(f.getUid());
		});
		return fr.getInfo();
	}
	
	public FlowRuner runManual( String uid ) {
		var p= runPoolDao.getPool(uid);
		return p.createAndStart();
	}

	public List<FlowPoolInfo> listFlowPoolInfo() {
		return runPoolDao.listPools().stream().map(p -> new FlowPoolInfo(p.getModel(), p.listRunInfos()))
				.collect(Collectors.toList());
	}

	public FlowPoolInfo getFlowPoolInfo(String uid) {
		var p = runPoolDao.getPool(uid);
		return new FlowPoolInfo(p.getModel(), p.listRunInfos());
	}

	public void forceRemove(String fuid) {
		runPoolDao.forceRemove(fuid);
	}

	public void testAction(ActionDebugData data, long delDelay) throws InterruptedException {
		String uid = RandomStringUtils.randomAlphanumeric(64);
		data.getFlow().setUid(uid);
		data.getFlow().setVars(data.getFlowVar());
		ActionFindInfo afi = data.getFlow().getAction(data.getUid());
		FlowRuner fr = dao.create(data.getFlow(),r-> {});
		BoxRuner br = fr.createBoxByUid(afi.getBox().getUid());
		br.setVars(data.getBoxVar());
		br.runAction(data.getUid());
		if (delDelay > 0) {
			Thread.sleep(delDelay);
		}
		boxDao.remove(br);
		dao.remove(fr.getInfo().getUid());
	}
}
