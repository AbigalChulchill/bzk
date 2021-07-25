package net.bzk.flow.infra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.Constant;
import net.bzk.infrastructure.PolyglotUtils;
import net.bzk.infrastructure.tscurve.TsCurveFunc;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.springframework.http.HttpMethod;

import lombok.Builder;
import net.bzk.flow.model.Action.Polyglot;
import net.bzk.flow.model.RunLog.RunState;
import net.bzk.flow.run.service.FastVarQueryer;
import net.bzk.flow.run.service.RunLogService;
import net.bzk.infrastructure.JsonUtils;

@Slf4j
public class PolyglotEngine {


    public enum CodeMember{
        bzk,tsFunc
    }

    public <R> R parseScriptbleText(String plain, Class<R> clz) {
        Object o = null;
        if (plain.startsWith(Constant.SCRIPT_PREFIX)) {
            plain = plain.replaceFirst(Constant.SCRIPT_PREFIX, "");
            o = parseCode(Polyglot.js.toString(), plain);
        } else {
            o = JsonUtils.stringToValue(plain);
        }
        if (o == null) return null;
        return JsonUtils.toByJson(o, clz);
    }


    public Object parseCode(String polyglot, String ifCode) {

        return PolyglotUtils.parseCode(polyglot, ifCode,
                s -> logMsg(s),
                s -> logWarn(s), genVarQueryer());
    }

    public Map<String, Object> genVarQueryer() {
        TsCurveFunc tsFunc = TsCurveFunc.getInstance();
        return PolyglotUtils.genSingleMap(CodeMember.tsFunc.toString(), tsFunc);
    }

    public void logMsg(String s) {
        log.info(s);
    }

    public void logWarn(String s) {
        log.warn(s);
    }


    public static Map<String, Object> genVarQueryerMap(FastVarQueryer vq) {
        var ans = PolyglotUtils.genSingleMap("bzk", vq);
        ans.put("testcall", new TODOtsdb());
        return ans;
    }

    @Builder
    public static class FlowPolyglotEngine extends PolyglotEngine {
        private FastVarQueryer varQueryer;
        private RunLogService logUtils;

        @Override
        public Map<String, Object> genVarQueryer() {
            var ans = super.genVarQueryer();
            ans.putAll(genVarQueryerMap(varQueryer));
            return ans;
        }

        @Override
        public void logMsg(String s) {
            logUtils.logWithMsg(varQueryer.getUids(), RunState.PolyglotExecute, s);
        }

        @Override
        public void logWarn(String s) {
            logUtils.logWithMsg(varQueryer.getUids(), RunState.ScriptError, s);
        }
    }

    public static class TODOTimeMap extends HashMap<String, Double> {
        public TODOTimeMap(Map<? extends String, ? extends Double> m) {
            super(m);
        }
    }

    @Data
    public static class TODOoutput {
        private String string;
        private float ff;
    }

    public static class TODOtsdb {

        public TODOoutput test(Map inm) {
            System.out.println(inm);
            TODOTimeMap m = new TODOTimeMap(inm);
            String all = "";
            for (var kv : m.entrySet()) {
                all += kv.getKey() + " " + kv.getValue();
            }
            TODOoutput ans = new TODOoutput();
            ans.string = all;
            ans.ff = all.length();
            return ans;
        }

    }


    public static void main(String args[]) {
        String ii = "GET";
        Object d = JsonUtils.stringToValue(ii);
        HttpMethod ss = JsonUtils.toByJson(d, HttpMethod.class);
        System.out.println("d=" + ss);


    }

}
