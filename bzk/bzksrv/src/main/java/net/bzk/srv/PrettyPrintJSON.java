package net.bzk.srv;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.*;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.graalvm.polyglot.*;
import org.springframework.stereotype.Component;

@Component
public class PrettyPrintJSON {

	@PostConstruct
	public void testJsRun() throws java.io.IOException {
		System.out.println("testJsRun");
		String input = "{\"aaa\":\"bb\"}";
		try (Context context = Context.create("js")) {
			Value parse = context.eval("js", "JSON.parse");
			Value stringify = context.eval("js", "JSON.stringify");
			Value result = stringify.execute(parse.execute(input), null, 2);
			System.out.println("testJsRun:"+result.asString());
			
			URL u = new File("C:\\Users\\DDT\\Desktop\\BZK\\BZKSRVJs\\TestCall\\app.js").toURI().toURL();
			String jscont= IOUtils.toString(u,StandardCharsets.UTF_8);
			context.eval("js", jscont);
		}
		
		
	}
}