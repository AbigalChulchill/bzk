package net.bzk.infrastructure;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

public class StrPlacer {
	
	private String plain;
	private Map<String, String> data = new HashMap<String, String>();
	
	private StrPlacer(String p) {
		plain = p;
	}
	
	public StrPlacer place(String key,String value) {
		data.put(key, value);
		return this;
	}
	
	public String replace() {
		return StringSubstitutor.replace(plain, data);
	}
	
	
	public static StrPlacer build(String p) {
		//TODO use obj pool
		return new StrPlacer(p);
	}
	
	

}
