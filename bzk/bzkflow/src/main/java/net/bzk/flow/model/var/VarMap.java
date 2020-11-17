package net.bzk.flow.model.var;

import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.obj.JsonMap;

@SuppressWarnings("serial")
public class VarMap extends JsonMap {

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}

	@FunctionalInterface
	public static interface VarProvider {
		VarMap getVars();
	}

	public static interface VarsDao {
		VarProvider getByUid(String uid);
	}

}
