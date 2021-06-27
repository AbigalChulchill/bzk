package net.bzk.flow;

public class Constant {
	
	public static final int MODEL_UID_SIZE = 12;
	public static final int RUN_UID_SIZE = 10;
	public static final String SCRIPT_PREFIX = "!javascript===";

	public static String sysVarKeyPrefix(String key){
		return "__"+key+"_";
	}

	public static String subTagKey(String flowName){
		return sysVarKeyPrefix(flowName+"_tag");
	}

	public static String subStateKey(String flowName){
		return sysVarKeyPrefix(flowName+"_state");
	}

}
