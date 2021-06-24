package net.bzk.infrastructure;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public class PolyglotUtils {

    public static Map<String, Object> genSingleMap(String k, Object v) {
        Map<String, Object> m = new HashMap<>();
        m.put(k, v);
        return m;
    }

    public static Object parseCode(String polyglot, String ifCode, Consumer<String> info, Consumer<String> er, Map<String, Object> members) {
        info.accept(ifCode);
        Object o = JsonUtils.stringToValue(ifCode);
        if (o == null)
            return null;
        info.accept(o.getClass() + " " + o);
        if (o instanceof String) {
            try {
                info.accept(o.toString());
                Object ans = callPolyglot(polyglot, o.toString(), members);
                info.accept("callPolyglot " + ans.getClass() + " " + ans);
                return ans;
            } catch (Exception e) {
                er.accept(e.getMessage());
            }
        }
        return o;
    }

    public static Object callPolyglot(String polyglot, String code, Map<String, Object> members) {
        try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
            for (var prs : members.entrySet()) {
                context.getBindings(polyglot).putMember(prs.getKey(), prs.getValue());
            }
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

}
