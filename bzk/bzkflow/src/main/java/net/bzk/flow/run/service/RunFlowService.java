package net.bzk.flow.run.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import net.bzk.flow.api.dto.ActionDebugData;
import net.bzk.flow.api.dto.RegisteredFlow;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.Flow.ActionFindInfo;
import net.bzk.flow.model.Entry.FixedRateEntry;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.dao.RunFlowDao;
import net.bzk.flow.run.dao.RunFlowPool;
import net.bzk.flow.run.dao.RunFlowPoolDao;
import net.bzk.flow.run.flow.BoxRuner;
import net.bzk.flow.run.flow.FlowRuner;
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
		if (!b)
			throw new BzkRuntimeException("exist :" + f);
	}

	public List<RegisteredFlow> listRegisters() {
		return runPoolDao.listPools().stream().map(p -> new RegisteredFlow(p.getModel(), p.listRunInfos()))
				.collect(Collectors.toList());
	}

	public void forceRemove(String fuid) {
		runPoolDao.forceRemove(fuid);
	}

	public void debugAction(ActionDebugData data, long delDelay) throws InterruptedException {
		String uid = RandomStringUtils.randomAlphanumeric(64);
		data.getFlow().setUid(uid);
		data.getFlow().setVars(data.getFlowVar());
		ActionFindInfo afi = data.getFlow().getAction(data.getUid());
		FlowRuner fr = dao.create(data.getFlow());
		BoxRuner br = fr.createBoxByUid(afi.getBox().getUid());
		br.setVars(data.getBoxVar());
		br.runAction(data.getUid());
		if (delDelay > 0) {
			Thread.sleep(delDelay);
		}
		boxDao.remove(br);
		dao.remove(fr.getUid());

	}
}
