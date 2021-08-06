package net.bzk.flow.run.service;

import net.bzk.flow.dto.JobRunInfo;
import net.bzk.flow.run.dao.ArchiveRunDao;
import net.bzk.flow.run.dao.JobsDao;
import net.bzk.flow.run.dao.RunFlowPoolDao;
import net.bzk.flow.run.flow.FlowRuner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Comparator;

@Service
@Scope("prototype")
public class JobRunInfoGetter {
    @Inject
    private JobsDao dao;
    @Inject
    private RunFlowPoolDao runPoolDao;
    @Inject
    private ArchiveRunDao archiveRunDao;

    private String uid;
    private JobRunInfo result = new JobRunInfo();

    JobRunInfoGetter init(String uid) {
        this.uid = uid;
        return this;
    }

    JobRunInfo fetch() {
        result.setUid(uid);
        result.setEnable(isEnable());
        int all = collectStateCount();
        result.setAllCount(all);
        var last = getLast();
        if(last == null) return result;
        result.setLastState(last.getState());
        result.setLastStartAt(last.getStartAt());
        return result;
    }

    private FlowRuner.RunInfo getLast() {
        var pool = runPoolDao.getPool(uid);
        if (pool == null) return null;
        var lfro = pool
                .listRunInfos(false)
                .stream()
                .sorted((r1, r2) -> -1 * r1.getStartAt().compareTo(r2.getStartAt()))
                .findFirst();
        if (lfro.isPresent()) return lfro.get();
        var laste = archiveRunDao.findTopByFlowUidOrderByCreateAtDesc(uid);
        return laste.isPresent() ? laste.get().getInfo() : null;

    }

    private int collectStateCount() {
        int all = 0;
        var cmap = result.getStateCounts();
        for (var st : FlowRuner.State.values()) {
            int c = getStateCount(st);
            int orgc = cmap.containsKey(st) ? cmap.get(st) : 0;
            result.getStateCounts().put(st, orgc + c);
            all += c;
        }
        return all;
    }

    private int getStateCount(FlowRuner.State state) {
        int ans = 0;
        long ai = archiveRunDao.countByFlowUidAndState(uid, state);
        if (ai > 0) ans += ai;
        if (result.isEnable()) ans += getRunStateCount(state);
        return ans;
    }

    private boolean isEnable() {
        return runPoolDao.exist(uid);
    }

    private int getRunStateCount(FlowRuner.State state) {
        return (int) runPoolDao
                .getPool(uid)
                .listRunInfos(false)
                .stream()
                .filter(r -> r.getState() == state)
                .count();
    }

}
