package net.bzk.flow.infra;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.Constant;
import net.bzk.flow.enums.Enums;
import net.bzk.infrastructure.PolyglotUtils;
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
                s -> logWarn(s), genCodeToolkit());
    }

    public Map<String, Object> genCodeToolkit() {
        TsCurveFunc tsFunc = TsCurveFunc.getInstance();
        Map<String, Object> ans = PolyglotUtils.genSingleMap(Enums.CodeMember.tsFunc.getCode(), tsFunc);
        ans.put(Enums.CodeMember.pe.getCode(), this);
        ans.put(Enums.CodeMember.orderUtils.getCode(), OrderUtils.getInstance());
        return ans;
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


    }

}
