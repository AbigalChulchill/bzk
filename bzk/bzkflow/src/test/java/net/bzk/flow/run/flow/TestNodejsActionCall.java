package net.bzk.flow.run.flow;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import net.bzk.flow.model.Action.NodejsAction;
import net.bzk.flow.run.action.NodejsActionCall;

@SpringBootTest
public class TestNodejsActionCall {
	
	@Inject
	private ApplicationContext context;
	
	@Test
	void testCreateByMode() {
		NodejsAction na = new NodejsAction();
		NodejsActionCall nj= (NodejsActionCall)context.getBean(na.getClazz());
		assertNotNull(nj);
	}

}
