package net.bzk.infrastructure;

import java.util.List;
import java.util.Map;

import org.graalvm.polyglot.Value;

public class PolyglotUtils {
	
	
	public static Object fixListObj(Map pm,String key) {
		Object ans = pm.get(key);
		if (ans instanceof Map) {
			if (((Map) ans).size() > 0) {
				return ans;
			}
//			if (function.getArraySize() > 0) {
//				ans = function.as(List.class);
//				return JsonUtils.toByJson(ans, Object.class);
//			}
		}
		return ans;
	}

}
