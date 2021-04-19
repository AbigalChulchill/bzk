package net.bzk.flow.run.action;

import java.util.HashMap;
import java.util.Map;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.model.Action.PolyglotAction;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.model.var.VarValSet;

@Service("net.bzk.flow.model.Action$PolyglotAction")
@Scope("prototype")
@Slf4j
public class PolyglotActionCall extends ActionCall<PolyglotAction> {

	public PolyglotActionCall() {
		super(PolyglotAction.class);
	}

	@Override
	public VarValSet call() throws Exception {
//		if(getModel().getUid().equals("1pfZVtQ4gnmO")) {
//			System.out.println("DEBUG");
//		}
		VarValSet set = new VarValSet();
		String code = getModel().getCode();
		logUtils.logActionCall( getUids(), "code: " + code);
		Object ans = getPolyglotEngine().callPolyglot( getModel().getPolyglot().toString(), code);
		logUtils.logActionCall( getUids(), "get ans " + ans);
		if (ans != null) {
			VarVal vv = new VarVal();
			vv.setKey(getModel().getResultKey());
			vv.setVal(ans);
			vv.setLv(getModel().getResultLv());
			set.add(vv);
		}
		return set;
	}

	public static void main(String[] args) {
		try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
			Map<String, Integer> m = new HashMap<>();
			m.put("test", 110);
			context.getBindings("js").putMember("map", m);
			Value function = context.eval("js", "[1,2,3,4,5]");
			Object ans = function.as(Object.class);
			if (ans instanceof Map) {
				Map map = (Map) ans;
				System.out.println(map.toString());
			}
			System.out.println(ans.getClass() + " " + ans);

		}

	}

}
