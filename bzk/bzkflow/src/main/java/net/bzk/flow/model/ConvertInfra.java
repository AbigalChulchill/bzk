package net.bzk.flow.model;

import java.util.ArrayList;
import java.util.List;

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
	
	public static class VarValList extends ArrayList<VarVal>{
		
	}
	
	public static class VarValListConvert extends JsonPojoConverter<VarValList> {

		@Override
		public Class<VarValList> getTClass() {
			return VarValList.class;
		}
	}
}
