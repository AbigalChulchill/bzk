package net.bzk.flow.run.action;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.NotSupportedException;

import net.bzk.flow.model.RunLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.model.Action.SysAction;
import net.bzk.flow.model.var.VarValSet;
import net.bzk.flow.run.dao.RunLogDao;

@Service("net.bzk.flow.model.Action$SysAction")
@Scope("prototype")
@Slf4j
public class SysActionCall extends ActionCall<SysAction> {

	@Inject
	private RunLogDao logDao;

	public SysActionCall() {
		super(SysAction.class);
	}

	@Override
	public VarValSet call() throws Exception {
		String func = getModel().getFunc();
		Map<String, ?> m = getPolyglotEngine().parseScriptbleText(getModel().getData(), Map.class);
		if(StringUtils.equals(func, "deleteLogs")) {
			deleteLogs(m);
			return new VarValSet();
		}
		throw new NotSupportedException("this "+func+" not suport!");
	}


	private void deleteLogs(Map<String, ?> m) {
		Date date = Date.from(Instant.parse(m.get("date").toString()));
//		logDao.deleteByCreateAtBefore(date);
		Pageable pageable = PageRequest.of(0, 100);
		Page<RunLog> logp = logDao.findByCreateAtBefore(date,pageable);
		while(true) {
			logDao.deleteAll(logp.getContent());
			if (!logp.hasNext()) {
				break;
			}
			pageable = logp.nextPageable();
		}
	}

}
