package net.bzk.flow.run.action;

import org.mariuszgromada.math.mxparser.*;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestMXparserActionCall {

	@Test
	void testRunExpression() throws Exception {

		Expression e = new Expression("2+1");
		assertEquals(e.calculate(), 3, 0);
	}

}
