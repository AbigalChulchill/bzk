package net.bzk.flow.run.service;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.bzk.flow.api.dto.DtoVarQuery;
import net.bzk.flow.model.Condition.PlainVal;
import net.bzk.flow.model.Condition.RefVal;
import net.bzk.flow.model.Condition.Val;
import net.bzk.flow.model.var.VarLv;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.model.var.VarMap.VarProvider;
import net.bzk.flow.model.var.VarMap.VarsDao;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.dao.RunFlowDao;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
public class RunVarService {

	@Inject
	private RunBoxDao boxDao;
	@Inject
	private RunFlowDao flowDao;

	public Optional<Object> findByQuery(DtoVarQuery q) {
		VarLv p = q.getPoint();
		return findValVal(p, q.getLvUid(), q.getKey());
	}

	public VarsDao getDao(VarLv l) {
		switch (l) {
		case not_specify:
		case run_box:
			return boxDao;
		case run_flow:
			return flowDao;

		}
		throw new BzkRuntimeException("Not Support the lv:" + l);
	}

	public VarProvider getProvider(VarLv l, String uid) {
		VarsDao dao = getDao(l);
		return dao.getByUid(uid);
	}

	public Optional<Object> findValVal(VarLv l, String uid, String key) {
		VarMap vs = getProvider(l, uid).getVars();
		if (!vs.containsKey(key))
			return Optional.empty();
		return Optional.of(vs.get(key) + "");
	}

	public VarMap getFlowVarMap(String uid) {
		return getProvider(VarLv.run_flow, uid).getVars();
	}

	public void putVarVal(Uids uids, VarVal val) {
		VarMap vs = getProvider(val.getLv(), uids.getLvUid(val.getLv())).getVars();
		vs.putByPath(val.getKey(), val.getVal());
	}

	public void putVarVals(Uids uids, VarValSet vvs) {
		for (VarVal vv : vvs.list()) {
			putVarVal(uids, vv);
		}
	}

	public Object getByVal(Val v) {
		if (v instanceof PlainVal) {
			PlainVal ov = (PlainVal) v;
			return ov.getRealVal();
		}
		if (v instanceof RefVal) {
			RefVal rv = (RefVal) v;
			return findByQuery(rv.getQuery()).get();
		}
		throw new BzkRuntimeException("not suppoert this v:" + v);
	}

}
