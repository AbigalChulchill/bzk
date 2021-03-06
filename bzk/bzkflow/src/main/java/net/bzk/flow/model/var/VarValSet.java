package net.bzk.flow.model.var;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import net.bzk.flow.enums.VarLv;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import lombok.Data;
import lombok.NoArgsConstructor;
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
		VarValSet ans = new VarValSet();
		if (StringUtils.isBlank(key))
			return ans;
		VarVal vv = new VarVal();
		vv.setKey(key);
		vv.setLv(lv);
		vv.setVal(val);
		ans.add(vv);
		return ans;
	}

	public static VarValSet genError(Action a, Exception val) {
		return genSingle(a.getErrorVarKey(), VarLv.run_box, new ActionError(val).toMap());
	}

	@Data
	@NoArgsConstructor
	public static class ActionError {
		private String clazz;
		private String exception;
		private String msg;

		ActionError(Exception e) {
			clazz = e.getClass().getCanonicalName();
			exception = ExceptionUtils.getStackTrace(e);
			msg = e.getMessage();
		}

		Map toMap() {
			return JsonUtils.toByJson(this, Map.class);
		}

	}

}
