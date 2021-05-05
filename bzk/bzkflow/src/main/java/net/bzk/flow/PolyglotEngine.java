package net.bzk.flow;

import java.util.List;
import java.util.Map;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.http.HttpMethod;

import lombok.Builder;
import net.bzk.flow.model.Action.Polyglot;
import net.bzk.flow.model.RunLog.RunState;
import net.bzk.flow.run.service.FastVarQueryer;
import net.bzk.flow.run.service.RunLogService;
import net.bzk.infrastructure.JsonUtils;

@Builder
public class PolyglotEngine {

	private FastVarQueryer varQueryer;
	private RunLogService logUtils;

	public <R> R parseScriptbleText(String plain, Class<R> clz) {
		Object o = null;
		if (plain.startsWith(Constant.SCRIPT_PREFIX)) {
			plain = plain.replaceFirst(Constant.SCRIPT_PREFIX, "");
			o = parseCode(Polyglot.js.toString(), plain);
		} else {
			o = JsonUtils.stringToValue(plain);
		}
		if(o==null) return null;
		return JsonUtils.toByJson(o, clz);
	}

	public Object parseCode(String polyglot, String ifCode) {
		logUtils.logWithMsg(varQueryer.getUids(),RunState.PolyglotExecute, ifCode);
		Object o = JsonUtils.stringToValue(ifCode);
		if (o == null)
			return null;
		logUtils.logWithMsg(varQueryer.getUids(),RunState.PolyglotExecute, o.getClass() + " " + o);
		if (o instanceof String) {
			try {
				logUtils.logWithMsg(varQueryer.getUids(),RunState.PolyglotExecute, o.toString());
				Object ans = callPolyglot(polyglot, o.toString());
				logUtils.logWithMsg(varQueryer.getUids(),RunState.PolyglotExecute, "callPolyglot " + ans.getClass() + " " + ans);
				return ans;
			} catch (Exception e) {
				logUtils.logActionCallWarn(varQueryer.getUids(), e.getMessage());
			}
		}
		return o;
	}

	public Object callPolyglot(String polyglot, String code) {
		try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
			context.getBindings(polyglot).putMember("bzk", varQueryer);
			Value function = context.eval(polyglot, code);
			Object ans = function.as(Object.class);
			return fixListObj(ans, function);
		}
	}

	public static Object fixListObj(Object ans, Value function) {
		if (ans instanceof Map) {
			if (((Map) ans).size() > 0) {
				return JsonUtils.toByJson(ans, Object.class);
			}
			if (function.getArraySize() > 0) {
				ans = function.as(List.class);
				return JsonUtils.toByJson(ans, Object.class);
			}
		}
		return JsonUtils.toByJson(ans, Object.class);
	}

	public static void main(String args[]) {
		String ii = "GET";
		Object d = JsonUtils.stringToValue(ii);
		HttpMethod ss = JsonUtils.toByJson(d, HttpMethod.class);
		System.out.println("d=" + ss);
		
		

	}

}
