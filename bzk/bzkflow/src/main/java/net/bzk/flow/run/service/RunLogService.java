package net.bzk.flow.run.service;

import java.util.function.Consumer;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.bzk.flow.enums.Enums;
import net.bzk.infrastructure.JsonUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.bzk.flow.model.Box;
import net.bzk.flow.model.RunLog;
import net.bzk.flow.enums.Enums.RunState;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.dao.RunBoxDao;
import net.bzk.flow.run.dao.RunFlowDao;
import net.bzk.flow.run.dao.RunLogDao;
import net.bzk.flow.run.flow.BoxRuner;
import net.bzk.flow.run.flow.FlowRuner;
import net.bzk.infrastructure.convert.Overwrite;

@Slf4j
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

    public void logActionCall(Uids u, String msg) {
        log(u,Enums.RunState.ActionCall,brl->{
            brl.setState(Enums.RunState.ActionCall);
            brl.setMsg(msg);
        });
    }

    public void logWithMsg(Uids u, RunState rs, String msg) {
        log(u,Enums.RunState.ActionCall,brl->{
            brl.setState(rs);
            brl.setMsg(msg);
        });
    }

    public void logActionCallWarn(Uids u, String wmsg) {
        RunLog brl = genLog(u);
        brl.setState(Enums.RunState.ActionCallWarn);
        brl.setMsg(wmsg);
        brl.setLogLv(Enums.LogLv.WARNING);
        dao.save(brl);
    }


    public void log(Uids u, RunState sr) {
        log(u, sr, l -> {
        });
    }

    @Transactional
    public void log(Uids u, RunState sr, Consumer<RunLog> cs) {
        try {
            var ans = genLog(u);
            ans.setState(sr);
            cs.accept(ans);
            if (!isEnable(u,ans))
                return;
            dao.save(ans);
        } catch (Exception e) {
            var us = JsonUtils.toJson(u);
            log.error(us + " " + sr, e);
        }

    }

    private boolean isEnable(Uids u,RunLog ans) {
        Enums.LogLv llv = ans.getLogLv();
        BoxRuner br = runBoxDao.getByUid(u.getRunBoxUid());
        Box b = br.getModel();
        Enums.LogLv bmLv = b.getMinLogLv();
        return llv.getLv() >= bmLv.getLv();
    }

    public RunLog genLog(Uids u) {
        if (u == null) {
            return new RunLog();
        }
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
        return setupUids(ans, u);
    }

    public RunLog setupUids(RunLog rl, Uids u) {
        RunLog ans = Overwrite.overwrite(u, rl);
        return ans;
    }

}
