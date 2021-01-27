package net.bzk.flow.utils;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.model.Action;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.dao.RunFlowDao;
import net.bzk.flow.run.flow.BoxRuner;
import net.bzk.flow.run.flow.FlowRuner;
import net.bzk.infrastructure.AES256Util;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
public class LogUtils {

	private static final String Encrypt_KEY_PREFIX = "MI87nnw)mi1";
	private static final String LOCATE_BOX_TAG_PREFIX = "<B%";
	private static final String LOCATE_BOX_TAG_SUBFIX = "%B>";
	private static final String LOCATE_ACTION_TAG_PREFIX = "<A%";
	private static final String LOCATE_ACTION_TAG_SUBFIX = "%A>";
	@Inject
	private RunBoxDao runBoxDao;
	@Inject
	private RunFlowDao runFlowDao;

	public void logActionCall(Logger l, Uids u, String msg) {
		BoxRuner br = runBoxDao.getByUid(u.getRunBoxUid());
		BoxRunLog brl = new BoxRunLog(br);
		brl.setUids(u);
		brl.setState(BoxRunState.ActionCall);
		brl.setMsg(msg);
		logRun(l, brl, LOCATE_ACTION_TAG_PREFIX, LOCATE_ACTION_TAG_SUBFIX);
	}

	public void logActionCallWarn(Logger l, Uids u, String wmsg) {
		BoxRuner br = runBoxDao.getByUid(u.getRunBoxUid());
		BoxRunLog brl = new BoxRunLog(br);
		brl.setUids(u);
		brl.setState(BoxRunState.ActionCallWarn);
		brl.setMsg(wmsg);
		brl.setFailed(true);
		logRun(l, brl, LOCATE_ACTION_TAG_PREFIX, LOCATE_ACTION_TAG_SUBFIX);
	}

	public void logBoxRun(Logger l, BoxRunLog brl) {

		logRun(l, brl, LOCATE_BOX_TAG_PREFIX, LOCATE_BOX_TAG_SUBFIX);
	}

	public void logRun(Logger l, BoxRunLog brl, String ts, String te) {
		FlowRuner fr = runFlowDao.getByUid(brl.uids.getRunFlowUid());
		try {
			String lm = JsonUtils.toJson(brl);
			l.info(ts + AES256Util.encrypt(fr.getModel().getLogEncryptKey(), lm) + te);
		} catch (Exception e) {
			throw new BzkRuntimeException(e);
		}
	}

	public static enum BoxRunState {
		BoxStart, BoxLoop, BoxLoopDone, EndFlow, LinkTo, StartAction, EndAction, ActionCall, ActionCallFail,
		ActionCallWarn, ActionResult, WhileLoopBottom,

	}

	@Data
	public static class BoxRunLog {

		private Uids uids;
		private String msg;
		private VarMap flowVar;
		private VarMap boxVar;
		private String boxName;
		private BoxRunState state;
		private boolean failed = false;
		private Exception exception;
		private String exceptionClazz;

		public BoxRunLog(BoxRuner br) {
			uids = br.genUids();
			boxName = br.getModel().getName();
			flowVar = br.getBundle().getFlowRuner().getVars();
			boxVar = br.getVars();
		}

		public BoxRunLog msg(String m) {
			msg = m;
			return this;
		}

		public BoxRunLog state(BoxRunState s) {
			state = s;
			return this;
		}

		public BoxRunLog failed(boolean b) {
			failed = b;
			return this;
		}

		public BoxRunLog exception(Exception e) {
			exception = BzkFlowUtils.toJsonSafeExcetption(e);
			exceptionClazz = e.getClass().getSimpleName();
			return this;
		}

	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class ActionRunLog extends BoxRunLog {
		private List<VarVal> varVals;
		private String actionName;

		public ActionRunLog(BoxRuner br, Action a) {
			super(br);
			actionName = a.getName();
		}

		public ActionRunLog varVals(List<VarVal> v) {
			varVals = v;
			return this;
		}

	}

}
