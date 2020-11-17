package net.bzk.flow.run.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.stereotype.Repository;

import net.bzk.flow.model.Flow;

@Repository
public class RunFlowPoolDao {
	@Inject
	private Provider<RunFlowPool> flowRunerPoolProvider;

	private Map<String, RunFlowPool> map = new ConcurrentHashMap<>();

	public boolean create(Flow fm) {
		if (map.containsKey(fm.getUid())) {
			return false;
		}
		RunFlowPool ans = flowRunerPoolProvider.get().init(fm);
		map.put(fm.getUid(), ans);
		return true;
	}

	public RunFlowPool getPool(String fUid) {
		return map.get(fUid);
	}

	public List<RunFlowPool> listPools() {
		return new ArrayList<>(map.values());
	}

	public void forceRemove(String fuid) {
		RunFlowPool rfp = map.get(fuid);
		rfp.forceCancelAll();
		map.remove(fuid);
	}

}
