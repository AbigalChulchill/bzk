package net.bzk.flow.run.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.bzk.flow.model.Flow;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.model.var.VarMap.VarsDao;
import net.bzk.flow.run.flow.FlowRuner;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Repository
public class RunFlowDao implements VarsDao {

	@Qualifier("BzkModelJsonMapper")
	@Inject
	private ObjectMapper jsonMapper;

	@Inject
	private Provider<FlowRuner> flowRunerProvider;

	private Map<String, FlowRuner> runMap = new ConcurrentHashMap<>();

	public FlowRuner create(Flow f) {
		try {
			String json = jsonMapper.writeValueAsString(f);
			f = jsonMapper.readValue(json, Flow.class);
			FlowRuner fr = flowRunerProvider.get().init(f, r -> remove(r.getInfo().getUid()));
			runMap.put(fr.getInfo().getUid(), fr);
			return fr;
		} catch (JsonProcessingException e) {
			throw new BzkRuntimeException(e);
		}
	}

	@Override
	public VarMap getVarMapByUid(String uid) {
		return runMap.get(uid).getVars();
	}

	public FlowRuner getByUid(String uid) {
		return runMap.get(uid);
	}

	public void remove(String uid) {
		runMap.remove(uid);
	}

}
