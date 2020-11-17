package net.bzk.flow.utils;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.bzk.flow.model.Action;
import net.bzk.flow.model.var.VarMap;
import net.bzk.flow.model.var.VarVal;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.flow.BoxRuner;
import net.bzk.infrastructure.JsonUtils;

@Service
public class LogUtils {

	private static final String LOCATE_BOX_TAG_PREFIX = "<B%";
	private static final String LOCATE_BOX_TAG_SUBFIX = "%B>";
	private static final String LOCATE_ACTION_TAG_PREFIX = "<A%";
	private static final String LOCATE_ACTION_TAG_SUBFIX = "%A>";
	@Inject
	private RunBoxDao runBoxDao;

	public void logActionCall(Logger l, Uids u, String format, Object... arguments) {
		String formattedStrin = java.text.MessageFormat.format(format, arguments);
		BoxRuner br = runBoxDao.getByUid(u.getBoxUid());
		BoxRunLog brl = new BoxRunLog(br);
		brl.setUids(u);
		brl.setState(BoxRunState.ActionCall);
		brl.setMsg(formattedStrin);
		logRun(l, brl, LOCATE_ACTION_TAG_PREFIX, LOCATE_ACTION_TAG_SUBFIX);

	}

	public static void logBoxRun(Logger l, BoxRunLog brl) {

		logRun(l, brl, LOCATE_BOX_TAG_PREFIX, LOCATE_BOX_TAG_SUBFIX);
	}

	public static void logRun(Logger l, BoxRunLog brl, String ts, String te) {
		String lm = JsonUtils.toJson(brl);
		l.info(ts + lm + te);
	}

	public static enum BoxRunState {
		BoxStart, EndFlow, LinkTo, StartAction, EndAction, ActionCall

	}

	@Data
	public static class BoxRunLog {

		private Uids uids;
		private String msg;
		private VarMap flowVar;
		private VarMap boxVar;
		private String boxName;
		private BoxRunState state;

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
