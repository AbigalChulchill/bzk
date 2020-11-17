package net.bzk.flow.api.dto;

import lombok.Data;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.var.VarMap;

@Data
public class ActionDebugData {

	private String uid;
	private Flow flow;
	private VarMap flowVar;
	private VarMap boxVar;

}
