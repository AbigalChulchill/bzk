package net.bzk.flow.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.bzk.auth.model.JsonPojoConverter;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.model.var.VarVal;

public class ConvertInfra {

	public static class VarMapConvert extends JsonPojoConverter<VarMap> {

		@Override
		public Class<VarMap> getTClass() {
			return VarMap.class;
		}
	}

	public static class VarValList extends ArrayList<VarVal> {

		public static VarValList gen(Collection<VarVal> vv) {
			VarValList ans = new VarValList();
			ans.addAll(vv);
			return ans;
		}

	}

	public static class VarValListConvert extends JsonPojoConverter<VarValList> {

		@Override
		public Class<VarValList> getTClass() {
			return VarValList.class;
		}
	}
	
	public static class MapConvert extends JsonPojoConverter<Map> {

		@Override
		public Class<Map> getTClass() {
			return Map.class;
		}
	}
}
