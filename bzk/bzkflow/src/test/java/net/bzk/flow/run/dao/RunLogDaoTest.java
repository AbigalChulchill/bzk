package net.bzk.flow.run.dao;

import javax.inject.Inject;

import net.bzk.flow.enums.Enums;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.service.RunLogService;

@SpringBootTest
public class RunLogDaoTest {
	@Inject
	private RunLogDao dao;
	@Inject
	private RunLogService service;
	
	@Test
	public void test() {
		Uids uids = new Uids();
		uids.setActionUid("test");
		uids.setRunFlowUid("test");
		uids.setRunBoxUid("test");
		service.log(uids, Enums.RunState.ActionCall);
		
//		var ans= dao.findByRunFlowUid("test");
//		
//		System.out.println( JsonUtils.toJson( ans.get(0)));
		
		
	}

}
