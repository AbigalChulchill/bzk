package net.bzk.flow.run.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Flow;
import net.bzk.flow.run.flow.FlowRuner;

@Repository
public class RunFlowDao {

	@Inject
	private Provider<FlowRuner> flowRunerProvider;

	private Map<String, FlowRuner> runMap = new ConcurrentHashMap<>();

	public FlowRuner create(RunFlowPool p,Flow f) {
		FlowRuner fr = flowRunerProvider.get().init(p,f);
		runMap.put(fr.getUid(), fr);
		return fr;
	}

	public FlowRuner getByUid(String uid) {
		return runMap.get(uid);
	}

}
