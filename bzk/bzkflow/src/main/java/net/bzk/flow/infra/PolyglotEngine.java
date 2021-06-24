package net.bzk.flow.infra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.Constant;
import net.bzk.infrastructure.PolyglotUtils;
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
        return null;
    }

    public void logMsg(String s) {
        log.info(s);
    }

    public void logWarn(String s) {
        log.warn(s);
    }


    public static Map<String, Object> genVarQueryerMap(FastVarQueryer vq) {
        return PolyglotUtils.genSingleMap("bzk", vq);
    }

    @Builder
    public static class FlowPolyglotEngine extends PolyglotEngine {
        private FastVarQueryer varQueryer;
        private RunLogService logUtils;

        @Override
        public Map<String, Object> genVarQueryer() {
            return genVarQueryerMap(varQueryer);
        }

        @Override
        public void logMsg(String s) {
            logUtils.logWithMsg(varQueryer.getUids(), RunState.PolyglotExecute, s);
        }

        @Override
        public void logWarn(String s) {
            logUtils.logActionCallWarn(varQueryer.getUids(), s);
        }
    }


    public static void main(String args[]) {
        String ii = "GET";
        Object d = JsonUtils.stringToValue(ii);
        HttpMethod ss = JsonUtils.toByJson(d, HttpMethod.class);
        System.out.println("d=" + ss);


    }

}
