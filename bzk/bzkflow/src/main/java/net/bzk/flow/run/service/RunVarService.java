package net.bzk.flow.run.service;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import lombok.Getter;
import net.bzk.flow.api.dto.DtoVarQuery;
import net.bzk.flow.model.var.VarLv;
import net.bzk.flow.model.var.VarMap.ProcVars;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.dao.RunFlowDao;
import net.bzk.infrastructure.obj.JsonMap;

@Service
public class RunVarService {

	@Getter
	@Inject
	private RunBoxDao boxDao;
	@Getter
	@Inject
	private RunFlowDao flowDao;

	public Optional<Object> findByQuery(DtoVarQuery q) {
		VarLv p = q.getPoint();
		return findValVal(q.getUids(), p, q.getKey());
	}
	


	private ProcVars genProcVars(Uids uids) {
		return new ProcVars(flowDao.getVarMapByUid(uids.getRunFlowUid()), boxDao.getVarMapByUid(uids.getRunBoxUid()));
	}
	
	public JsonMap getFlowVar(Uids uids) {
		return flowDao.getVarMapByUid(uids.getRunFlowUid());
	}

	public Optional<Object> findValVal(Uids uids, VarLv p, String key) {
		return genProcVars(uids).find(p, key);
	}

	public void putVarVal(Uids uids, VarVal val) {
		ProcVars pv = genProcVars(uids);
		pv.put(val.getLv(), val.getKey(), val.getVal());
	}

	public void putVarVals(Uids uids, VarValSet vvs) {
		for (VarVal vv : vvs.list()) {
			putVarVal(uids, vv);
		}
	}


}
