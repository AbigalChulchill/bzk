package net.bzk.flow.run.service;

import net.bzk.flow.dto.JobRunInfo;
import net.bzk.flow.model.Job;
import net.bzk.flow.run.dao.ArchiveRunDao;
import net.bzk.flow.run.dao.JobsDao;
import net.bzk.flow.run.dao.RunFlowPoolDao;
import net.bzk.flow.run.flow.FlowRuner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
@Scope("prototype")
public class JobRunInfoGetter {
    @Inject
    private JobsDao dao;
    @Inject
    private RunFlowPoolDao runPoolDao;
    @Inject
    private ArchiveRunDao archiveRunDao;

    private Job job;
    private String uid;
    private JobRunInfo result = new JobRunInfo();

    JobRunInfoGetter init(String uid) {
        this.uid = uid;
        this.job = dao.findById(uid).get();
        return this;
    }

    JobRunInfo fetch() {
        result.setUid(uid);
        result.setEnable(isEnable());
        setupCount();
        var last = getLast();
        result.setName(job.getModel().getName());
        if (last != null) {
            result.setLastState(last.getState());
            result.setLastStartAt(last.getStartAt());
        }
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
        var last = archiveRunDao.findTopByFlowUidOrderByCreateAtDesc(uid);
        return last.isPresent() ? last.get().getInfo() : null;

    }

    private void setupCount() {
        int all = 0, runc = 0, archivec = 0;
        var cmap = result.getStateCounts();
        for (var st : FlowRuner.State.values()) {
            int rc = getRunStateCount(st);
            int ac = getArchiveStateCount(st);
            int sumc = rc + ac;
            int orgc = cmap.containsKey(st) ? cmap.get(st) : 0;
            result.getStateCounts().put(st, orgc + sumc);
            all += sumc;
            runc += rc;
            archivec += ac;
        }
        result.setArchiveCount(archivec);
        result.setRunCount(runc);
        result.setAllCount(all);
        result.setJobVersion(job.getModel().getVersion());
        result.setModel(job.getModel());
        if (isEnable()) result.setRunVersion(runPoolDao.getPool(uid).getModel().getVersion());
    }


    private int getArchiveStateCount(FlowRuner.State state) {
        return (int) (long) archiveRunDao.countByFlowUidAndState(uid, state);

    }

    private boolean isEnable() {
        return runPoolDao.exist(uid);
    }

    private int getRunStateCount(FlowRuner.State state) {
        var p = runPoolDao.getPool(uid);
        if (p == null) return 0;
        return (int) p
                .listRunInfos(false)
                .stream()
                .filter(r -> r.getState() == state)
                .count();
    }

}
