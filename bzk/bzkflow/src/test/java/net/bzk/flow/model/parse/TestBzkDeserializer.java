package net.bzk.flow.model.parse;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Qualifier;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import net.bzk.flow.model.Action;
import net.bzk.flow.model.Action.NodejsAction;



@SpringBootTest
public class TestBzkDeserializer {

	@Qualifier("BzkModelJsonMapper")
	@Inject
	private ObjectMapper mapper;

	@Data
	public static class B {

		private String key = "1";
		private Action action;
		
		

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
