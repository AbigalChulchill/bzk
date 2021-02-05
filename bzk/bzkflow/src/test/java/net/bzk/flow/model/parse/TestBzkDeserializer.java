package net.bzk.flow.model.parse;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bzk.flow.model.Action;
import net.bzk.flow.model.Action.NodejsAction;



@SpringBootTest
public class TestBzkDeserializer {

	@Qualifier("BzkModelJsonMapper")
	@Inject
	private ObjectMapper mapper;

	public static class B {

		private String key = "1";
		private Action action;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}
		public Action getAction() {
			return action;
		}
		public void setAction(Action action) {
			this.action = action;
		}
		
		

	}

	@Test
	public void testDeserializerBzk() throws JsonProcessingException {

		B b = new B();
		b.setKey("qqq");
		NodejsAction na = new NodejsAction();
		na.setCode("xxx");
		b.setAction(na);

		String json = mapper.writeValueAsString(b);
		System.out.println(json);
		
		B bn= mapper.readValue(json, B.class);
		json = mapper.writeValueAsString(bn);
		System.out.println(json);
		NodejsAction ona= (NodejsAction) bn.getAction();
		assertEquals(ona.getCode(), "xxx");
	}

}
