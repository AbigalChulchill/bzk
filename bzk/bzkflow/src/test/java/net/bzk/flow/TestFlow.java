package net.bzk.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bzk.flow.api.FlowController;
import net.bzk.flow.model.Flow;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;

@SpringBootTest
public class TestFlow {
	
	@Value("classpath:test/model/testNextBox.json")
	private Resource testNextBoxJson;
	@Value("classpath:test/model/CalcBfxFundRange.json")
	private Resource calcBfxFundRange;	
	@Value("classpath:test/model/callTest_ThisA.json")
	private Resource callSubThisA;	
	@Value("classpath:test/model/callTest_ThisB.json")
	private Resource callSubThisB;	
	@Inject
	private FlowController flowController;
	@Inject
	@Qualifier("BzkModelJsonMapper")
	private ObjectMapper jsonMapper;
	
//	@Test
	public void testNextBoxJson() throws JsonMappingException, JsonProcessingException {
		String json= CommUtils.loadBy(testNextBoxJson);
		Flow f = jsonMapper.readValue(json, Flow.class);
		ArrayList<Flow> fs = new ArrayList<>();
		fs.add(f);
		var ans =flowController.testFlow(f.getUid(), fs);
		System.out.println(JsonUtils.toJson(ans) ); 
	}
	
//	@Test
	public void testCalcBfxFundRange() throws JsonMappingException, JsonProcessingException {
		String json= CommUtils.loadBy(calcBfxFundRange);
		Flow f = jsonMapper.readValue(json, Flow.class);
		ArrayList<Flow> fs = new ArrayList<>();
		fs.add(f);
		var ans =flowController.testFlow(f.getUid(), fs);
		System.out.println(JsonUtils.toJson(ans) ); 
	}
	
	@Test
	public void testCallSub() throws JsonMappingException, JsonProcessingException {
		String jsonA= CommUtils.loadBy(callSubThisA);
		String jsonB= CommUtils.loadBy(callSubThisB);
		Flow af = jsonMapper.readValue(jsonA, Flow.class);
		Flow bf = jsonMapper.readValue(jsonB, Flow.class);
		ArrayList<Flow> fs = new ArrayList<>();
		fs.add(af);
		fs.add(bf);
		var ans =flowController.testFlow(af.getUid(), fs);
		System.out.println(JsonUtils.toJson(ans) ); 
		System.out.println(JsonUtils.toJson(ans.getEndResult()));
		assertEquals(ans.getEndResult().size(), 3);
		var a1v= ans.getEndResult().stream().filter(e-> e.getKey().equals("A1")).findAny().get();
		assertNotNull(a1v);
		
	}

}
