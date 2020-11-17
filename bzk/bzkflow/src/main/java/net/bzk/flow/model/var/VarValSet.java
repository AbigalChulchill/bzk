package net.bzk.flow.model.var;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;

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

}
