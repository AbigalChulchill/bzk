package net.bzk.flow.model.var;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public enum VarLv {
	
	not_specify,run_flow,run_box;
	
	public static final String PRFIX_BOX = "@";
	public static final String PRFIX_FLOW = "~";
	public static final String PRFIX_NOT_SPECIFTY = "$";
	
	
	public static VarKey checkLvByPrefix(String fqe) {
		if (fqe.startsWith(PRFIX_BOX))
			return new VarKey(VarLv.run_box, fqe.replace(PRFIX_BOX, ""));
		if (fqe.startsWith(PRFIX_FLOW))
			return new VarKey(VarLv.run_flow, fqe.replace(PRFIX_FLOW, ""));
		if (fqe.startsWith(PRFIX_NOT_SPECIFTY))
			return new VarKey(VarLv.not_specify, fqe.replace(PRFIX_NOT_SPECIFTY, ""));
		return new VarKey(VarLv.not_specify, fqe);
	}
	
	@Data
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	@NoArgsConstructor
	public static class VarKey {

		private VarLv lv;
		private String key;

	}
	

}
