package net.bzk.flow.enums;

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
}
