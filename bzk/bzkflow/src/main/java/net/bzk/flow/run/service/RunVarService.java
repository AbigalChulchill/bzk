package net.bzk.flow.run.service;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.bzk.flow.api.dto.DtoVarQuery;
import net.bzk.flow.model.Condition.RefVal;
import net.bzk.flow.model.Condition.TxtVal;
import net.bzk.flow.model.Condition.Val;
import net.bzk.flow.model.var.BaseVar;
import net.bzk.flow.model.var.VarLv;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.dao.RunFlowDao;
import net.bzk.flow.run.flow.BoxRuner;
import net.bzk.flow.run.flow.FlowRuner;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
public class RunVarService {
	@Inject
	private RunBoxDao boxDao;
	@Inject
	private RunFlowDao flowDao;

	public Optional<String> findByQuery(DtoVarQuery q) {
		VarLv p = q.getPoint();
		switch (p) {
		case not_specify:
		case run_box:
			return findByRunBox(q.getUids().getRunBoxUid(), q.getKey());
		case run_flow:
			return findByRunFlow(q.getUids().getRunFlowUid(), q.getKey());

		}
		throw new BzkRuntimeException("Not Support the point:" + q.getPoint());

	}

	public Optional<String> findByRunBox(String rboxUid, String key) {
		BoxRuner br = boxDao.getByUid(rboxUid);
		BaseVar vs = br.getVars();
		if (!vs.containsKey(key))
			return Optional.empty();
		return Optional.of(vs.get(key) + "");
	}

	public Optional<String> findByRunFlow(String fUid, String key) {
		FlowRuner fr = flowDao.getByUid(fUid);
		BaseVar vs = fr.getVars();
		if (!vs.containsKey(key))
			return Optional.empty();
		return Optional.of(vs.get(key) + "");
	}

	public void putByRunBox(String uid, String key, String val) {
		BoxRuner fr = boxDao.getByUid(uid);
		BaseVar vs = fr.getVars();
		vs.putByPath(key, val);
	}

	public void putVarVal(Uids uids, VarVal val) {
		switch (val.getLv()) {
		case run_box:
			putByRunBox(uids.getRunBoxUid(), val.getKey(), val.getVal());
			return;
		case run_flow:
			putByRunFlow(uids.getRunFlowUid(), val.getKey(), val.getVal());
			return;
		case not_specify:
			throw new BzkRuntimeException("don`t used not_specify");
		}
		throw new BzkRuntimeException("not support lv:" + val.getLv());
	}

	public void putVarVals(Uids uids, VarValSet vvs) {
		for (VarVal vv : vvs.list()) {
			putVarVal(uids, vv);
		}
	}

	public void putByRunFlow(String uid, String key, String val) {
		FlowRuner fr = flowDao.getByUid(uid);
		BaseVar vs = fr.getVars();
		vs.putByPath(key, val);
	}

	public String getByVal(Val v) {
		if (v instanceof TxtVal) {
			TxtVal ov = (TxtVal) v;
			return ov.getVal();
		}
		if (v instanceof RefVal) {
			RefVal rv = (RefVal) v;
			return findByQuery(rv.getQuery()).get();
		}
		throw new BzkRuntimeException("not suppoert this v:" + v);
	}

}
