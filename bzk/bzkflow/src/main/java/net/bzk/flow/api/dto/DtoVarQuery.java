package net.bzk.flow.api.dto;

import lombok.Data;
import net.bzk.flow.model.var.VarLv;
import net.bzk.flow.run.action.ActionCall.Uids;

@Data
public class DtoVarQuery {
	
	
	private Uids uids;
	private VarLv point;
	private String key;

}
