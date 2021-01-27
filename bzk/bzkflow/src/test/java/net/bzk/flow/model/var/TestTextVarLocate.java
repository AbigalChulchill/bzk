package net.bzk.flow.model.var;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.PlaceholderUtils;

@SpringBootTest
public class TestTextVarLocate {

	@Test
	void testlistPlaceHolderKeys() {
		final String str = "Hi ${name.var}, how are you? I'm ${namevar2}.test ${!namevar2}. ${~namev.ar2}.    xxx  ";
		List<String> res = PlaceholderUtils.listStringSubstitutorKeys(str);
		System.out.println(res);
		assertEquals(res.size(), 4);

	}

	@Test
	void testNodeLocate() throws Exception {

		String txt = "<BZK!VAR@Start>{\"key\":\"v0.d\",\"lv\":\"run_flow\",\"val\":\"new!\"}<BZK@VAR#END>";
		TextVarLocate tl = new TextVarLocate();
		tl.parseLn(txt);
		System.out.println(JsonUtils.toJson(tl.list().list()));

	}

}
