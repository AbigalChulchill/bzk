package net.bzk.flow.infra;

import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.BzkConstant;
import net.bzk.flow.enums.Enums;
import net.bzk.infrastructure.PolyglotUtils;
import net.bzk.infrastructure.ordermgt.DirectionUtils;
import net.bzk.infrastructure.ordermgt.OrderUtils;
import net.bzk.infrastructure.tscurve.TsCurveFunc;
import org.springframework.http.HttpMethod;

import lombok.Builder;
import net.bzk.flow.enums.Polyglot;
import net.bzk.flow.run.service.FastVarQueryer;
import net.bzk.flow.run.service.RunLogService;
import net.bzk.infrastructure.JsonUtils;

@Slf4j
public class PolyglotEngine {


    public <R> R parseScriptbleText(String plain, Class<R> clz) {
        Object o = null;
        if (plain.startsWith(BzkConstant.SCRIPT_PREFIX)) {
            plain = plain.replaceFirst(BzkConstant.SCRIPT_PREFIX, "");
            o = parseCode(Polyglot.js.toString(), plain);
        } else {
            o = JsonUtils.stringToValue(plain);
        }
        if (o == null) return null;
        return JsonUtils.toByJson(o, clz);
    }


    public Object parseCode(String polyglot, String ifCode) {
        boolean jsonRetruned = ifCode.startsWith(BzkConstant.SCRIPT_RETURN_JSON_PREFIX);
        ifCode = ifCode.replaceFirst(BzkConstant.SCRIPT_RETURN_JSON_PREFIX, "");
        Object ans = PolyglotUtils.parseCode(polyglot, ifCode,
                s -> logMsg(s),
                s -> logWarn(s), genCodeToolkit());
        if (jsonRetruned) {
            return JsonUtils.loadByJson((String) ans, Object.class);
        }
        return ans;
    }

    public Map<String, Object> genCodeToolkit() {
        TsCurveFunc tsFunc = TsCurveFunc.getInstance();
        Map<String, Object> ans = PolyglotUtils.genSingleMap(Enums.CodeMember.tsFunc.getCode(), tsFunc);
        ans.put(Enums.CodeMember.pe.getCode(), this);
        ans.put(Enums.CodeMember.orderUtils.getCode(), OrderUtils.getInstance());
        ans.put(Enums.CodeMember.directionUtils.getCode(), DirectionUtils.getInstance());
        return ans;
    }

    public String toJson(Object o) {
        return JsonUtils.toJson(o);
    }

    public void logMsg(String s) {
        log.info(s);
    }

    public void logWarn(String s) {
        log.warn(s);
    }


    public static Map<String, Object> genVarQueryerMap(FastVarQueryer vq) {
        var ans = PolyglotUtils.genSingleMap(Enums.CodeMember.bzk.getCode(), vq);
        return ans;
    }

    @Builder
    public static class FlowPolyglotEngine extends PolyglotEngine {
        private FastVarQueryer varQueryer;
        private RunLogService logUtils;

        @Override
        public Map<String, Object> genCodeToolkit() {
            var ans = super.genCodeToolkit();
            ans.putAll(genVarQueryerMap(varQueryer));
            return ans;
        }

        @Override
        public void logMsg(String s) {
            logUtils.logWithMsg(varQueryer.getUids(), Enums.RunState.PolyglotExecute, s);
        }

        @Override
        public void logWarn(String s) {
            logUtils.logWithMsg(varQueryer.getUids(), Enums.RunState.ScriptError, s);
        }
    }


    public static void main(String args[]) {
        String ii = "GET";
        Object d = JsonUtils.stringToValue(ii);
        HttpMethod ss = JsonUtils.toByJson(d, HttpMethod.class);
        System.out.println("d=" + ss);
//        Map m = null;
//        Set<Map.Entry> es = m.entrySet();
//        for (Map.Entry e : es){
//            e.getKey();
//            e.getValue()
//        }
    }

}
