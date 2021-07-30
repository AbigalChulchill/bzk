package net.bzk.flow.api.dto;

import lombok.Data;
import net.bzk.flow.enums.VarLv;
import net.bzk.flow.run.action.ActionCall.Uids;

@Data
public class DtoVarQuery {

	private Uids uids;
	private VarLv point;
	private String key;

	public String getLvUid() {
		return uids.getLvUid(point);
	}

}
