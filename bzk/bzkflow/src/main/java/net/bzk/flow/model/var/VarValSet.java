package net.bzk.flow.model.var;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;

public class VarValSet {
	
	private List<VarVal> list = new CopyOnWriteArrayList<>();
	
	public VarValSet() {}
	
	public VarValSet(Collection<VarVal> vc) {
		list.addAll(vc);
	}
	
	public void add(VarVal vv) {
		list.removeIf(e-> e.getLv() == vv.getLv() && StringUtils.equals(e.getKey(), vv.getKey()));
		list.add(vv);
	}
	
	public List<VarVal> list(){
		return list;
	}

}
