package net.bzk.flow.run.dao;

import java.util.List;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.bzk.flow.model.RunLog;
import net.bzk.flow.run.action.ActionCall.Uids;
import net.bzk.flow.run.service.RunLogService;
import net.bzk.infrastructure.JsonUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@SpringBootTest
public class TestRunLogDao {

    @Inject
    private RunLogDao dao;

    @Inject
    private RunLogService service;

    @Test
    public void testSave() {
        RunLog rl = new RunLog();
        Uids u = new Uids();
        u.setRunFlowUid("fff");
        u.setActionUid("qqq");
        u.setRunActionUid("xxx");
        service.setupUids(rl, u);
        dao.save(rl);
        Pageable paging = PageRequest.of(0, 100);
        var lgs = dao.findByRunFlowUidOrderByCreateAtAsc("fff", paging);
        System.out.println(JsonUtils.toJson(lgs));
    }

}
