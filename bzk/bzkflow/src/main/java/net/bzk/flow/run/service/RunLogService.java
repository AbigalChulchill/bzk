package net.bzk.flow.run.service;

import java.util.function.Consumer;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.bzk.flow.model.Box;
import net.bzk.flow.model.RunLog;
import net.bzk.flow.model.RunLog.RunState;
import net.bzk.flow.run.action.ActionCall;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.dao.RunFlowDao;
import net.bzk.flow.run.dao.RunLogDao;
import net.bzk.flow.run.flow.BoxRuner;
import net.bzk.flow.run.flow.FlowRuner;

@Service
public class RunLogService {

//	private static final String Encrypt_KEY_PREFIX = "MI87nnw)mi1";
//	private static final String LOCATE_BOX_TAG_PREFIX = "<B%";
//	private static final String LOCATE_BOX_TAG_SUBFIX = "%B>";
//	private static final String LOCATE_ACTION_TAG_PREFIX = "<A%";
//	private static final String LOCATE_ACTION_TAG_SUBFIX = "%A>";

	@Inject
	private RunLogDao dao;
	@Inject
	private RunBoxDao runBoxDao;
	@Inject
	private RunFlowDao runFlowDao;

	public void logActionCall( Uids u, String msg) {
		RunLog brl = genLog(u);
		brl.setUids(u);
		brl.setState(RunState.ActionCall);
		brl.setMsg(msg);
		dao.save(brl);
	}

	public void logActionCallWarn( ActionCall ac, String wmsg) {
		RunLog brl = genLog(ac.getUids());
		brl.setState(RunState.ActionCallWarn);
		brl.setMsg(wmsg);
		brl.setFailed(true);
		dao.save(brl);
	}

	public void log(Uids u, RunState sr) {
		log(u, sr, l -> {
		});
	}

	public void log(Uids u, RunState sr, Consumer<RunLog> cs) {
		var ans = genLog(u);
		ans.setState(sr);
		cs.accept(ans);
		dao.save(ans);
	}

	public RunLog genLog(Uids u) {
		FlowRuner fr = runFlowDao.getByUid(u.getRunFlowUid());
		BoxRuner br = runBoxDao.getByUid(u.getRunBoxUid());
		Box b = br.getModel();
		var ao = b.findAction(u.getActionUid());
		RunLog ans = new RunLog();
		ans.setFlowVar(fr.getVars());
		ans.setBoxVar(br.getVars());
		ans.setBoxName(b.getName());
		if (ao.isPresent())
			ans.setActionName(ao.get().getName());
		ans.setUids(u);
		return ans;

	}

}
