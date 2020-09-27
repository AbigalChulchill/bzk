package net.bzk.flow;

import javax.annotation.PostConstruct;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.stereotype.Component;

@Component
public class PrettyPrintJSON {


	
	@PostConstruct
	public void testJsRun() throws java.io.IOException {
		String input = "{\"aaa\":\"bb\"}";
		try (Context context = Context.create("js")) {
			Value parse = context.eval("js", "JSON.parse");
			Value stringify = context.eval("js", "JSON.stringify");
			Value result = stringify.execute(parse.execute(input), null, 2);
			System.out.println("testJsRun:"+result.asString());
			
		}
		
		
	}
}