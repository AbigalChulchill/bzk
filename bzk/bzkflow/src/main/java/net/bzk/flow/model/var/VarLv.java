package net.bzk.flow.model.var;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public enum VarLv {
	
	not_specify,run_flow,run_box,run_sys;
	
	public static final String PREFIX_BOX = "@";
	public static final String PREFIX_FLOW = "~";
	public static final String PREFIX_SYS = "%";
	public static final String PREFIX_NOT_SPECIFY = "$";
	
	
	public static VarKey checkLvByPrefix(String fqe) {
		if (fqe.startsWith(PREFIX_BOX))
			return new VarKey(VarLv.run_box, fqe.replace(PREFIX_BOX, ""));
		if (fqe.startsWith(PREFIX_FLOW))
			return new VarKey(VarLv.run_flow, fqe.replace(PREFIX_FLOW, ""));
		if (fqe.startsWith(PREFIX_SYS))
			return new VarKey(VarLv.run_sys, fqe.replace(PREFIX_SYS, ""));
		if (fqe.startsWith(PREFIX_NOT_SPECIFY))
			return new VarKey(VarLv.not_specify, fqe.replace(PREFIX_NOT_SPECIFY, ""));
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
