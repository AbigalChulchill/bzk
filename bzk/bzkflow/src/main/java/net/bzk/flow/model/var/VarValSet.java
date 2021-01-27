package net.bzk.flow.model.var;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.model.Action;
import net.bzk.infrastructure.JsonUtils;

public class VarValSet {

	private List<VarVal> list = new CopyOnWriteArrayList<>();

	public VarValSet() {
	}

	public VarValSet(Collection<VarVal> vc) {
		list.addAll(vc);
	}

	public void add(VarVal vv) {
		list.removeIf(e -> e.getLv() == vv.getLv() && StringUtils.equals(e.getKey(), vv.getKey()));
		list.add(vv);
	}

	public List<VarVal> list() {
		return list;
	}
	
	

	@Override
	public String toString() {
		return JsonUtils.toJson(list);
	}

	public static VarValSet genSingle(String key, VarLv lv, Object val) {
		VarVal vv = new VarVal();
		vv.setKey(key);
		vv.setLv(lv);
		vv.setVal(val);
		VarValSet ans = new VarValSet();
		ans.add(vv);
		return ans;
	}
	
	public static VarValSet genError(Action a, Exception val) {
		return genSingle(a.getErrorVarKey(),VarLv.run_box,new ActionError(val));
	}
	
	@Data
	@NoArgsConstructor
	public static class ActionError{
		private String clazz;
		private Exception exception;
		private String msg;
		
		ActionError(Exception e){
			clazz = e.getClass().getCanonicalName();
			exception = BzkFlowUtils.toJsonSafeExcetption(e);
			msg = e.getMessage();
		}
		
	}

}
