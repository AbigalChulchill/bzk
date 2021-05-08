package net.bzk.flow.run.flow;

import java.time.ZonedDateTime;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import net.bzk.flow.model.Condition.ConditionTime;
import net.bzk.flow.model.Condition.TimeCheckType;
import net.bzk.flow.model.RunLog.RunState;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service("net.bzk.flow.model.Condition$ConditionTime")
@Scope("prototype")
public class ConditionTimer extends Conditioner<ConditionTime> {

	public ConditionTimer() {
		super(ConditionTime.class);
	}

	@Override
	public boolean checkSelf() {
		if (getModel().isNot()) {
			return !check();
		} else {
			return check();
		}

	}

	private boolean check() {
		String ls = getPolyglotEngine().parseScriptbleText(getModel().getLeft(), String.class);
		String rs = getPolyglotEngine().parseScriptbleText(getModel().getRight(), String.class);
		ZonedDateTime lat = parse(ls);
		ZonedDateTime rat = parse(rs);

		var ans = checkTime(lat, rat);
		logUtils.logWithMsg(getUids(), RunState.ConditionResult,
				lat + " is" + getModel().getType() + "( " + rat + " ) : " + ans);
		return ans;
	}

	private boolean checkTime(ZonedDateTime lat, ZonedDateTime rat) {
		switch (getModel().getType()) {
		case After:
			return lat.isAfter(rat);
		case Before:
			return lat.isBefore(rat);
		case Equal:
			return lat.isEqual(rat);

		}
		throw new BzkRuntimeException("not support type:" + getModel().getType());
	}

	private ZonedDateTime parse(String iso8601) {
		return ZonedDateTime.parse(iso8601);
	}

}
