package net.bzk.flow.run.action;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import net.bzk.flow.model.Action.NodejsAction;
import net.bzk.flow.run.action.ActionCall.Uids;
@SpringBootTest
public class TestNodejsActionCall {
	
	@Inject
	private ApplicationContext context;
	
	
	@Test
	void testRun() throws Exception {
		NodejsActionCall naer= context.getBean("net.bzk.flow.model.flow.Action$NodejsAction", NodejsActionCall.class);
		NodejsAction na =new NodejsAction();
		Map<String,String> dm = new HashMap<>();
		dm.put("moment", "^2.26.0");
		na.setDependencies(dm);
		na.setDevDependencies(new HashMap<>());
		na.setCode("console.log($bzk.init)");
		Uids uids = new Uids();
		uids.setRunBoxUid("qqq");
		uids.setRunFlowUid("zzz");
		naer.initBase(uids,na);
		naer.call();
		
	}

}
