package net.bzk.flow.enums;

import lombok.Getter;

public class Enums {

    public static enum ConvertMethod {
        ToJSONText;
    }

    public static enum RunState {
        BoxStart, BoxLoop, EndFlow, LinkTo,
        StartAction, EndAction, ActionCall, ActionCallFail,
        ActionCallWarn, ActionResult, WhileLoopBottom,
        ConditionFail, ModelReplaced, PolyglotExecute, ConditionResult, BoxError, ScriptError

    }

    public static enum LogLv {
        NONE(0), DEBUG(1), INFO(2), WARNING(3), ERROR(4);
        @Getter
        private int lv;

        private LogLv(int v) {
            lv = v;
        }
    }

    public enum CodeMember {
        bzk, tsFunc, pe , orderUtils;

        public String getCode() {
            return '$' + this.toString();
        }
    }
}
