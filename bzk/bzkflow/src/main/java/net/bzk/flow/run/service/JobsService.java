package net.bzk.flow.run.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;

import net.bzk.flow.enums.Enums;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.Job;
import net.bzk.flow.run.dao.JobsDao;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
public class JobsService {

    @Value("${init.data.path}")
    private String initDataPath;
    @Value("${init.data.saveback}")
    private boolean saveBackInited = false;
    @Inject
    private ApplicationEventPublisher applicationEventPublisher;
    @Inject
    private JobsDao dao;
    @Inject
    private ObjectMapper mapper;

    @PostConstruct
    public void loadInitData() {
        var dir = new File(initDataPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        var ffs = FileUtils.listFiles(dir, new String[]{"json"}, false);
        List<Flow> fls = ffs.stream().map(this::importByFile).collect(Collectors.toList());
        applicationEventPublisher.publishEvent(new InitFlowEvent(this, fls));
    }

    @Transactional
    private Flow importByFile(File f) {
        try {
            Flow flow = BzkFlowUtils.getFlowJsonMapper().readValue(f, Flow.class);
            boolean migrated = migrateBoxLogLv(flow);
            save(flow, migrated);
            return flow;
        } catch (IOException e) {
            throw new BzkRuntimeException(e);
        }
    }

    private boolean migrateBoxLogLv(Flow f) {
        boolean ans = false;
        for (var t : f.getBoxs()) {
            if (t.getMinLogLv() != null) continue;
            t.setMinLogLv(Enums.LogLv.WARNING);
            ans = true;
        }
        return ans;
    }


    @Transactional
    public Job save(Flow f, boolean saveFile) throws IOException {
        var sfo = dao.findById(f.getUid());
        Job sf = sfo.orElse(Job.gen(f));
        sf.setModel(f);
        if (saveBackInited && saveFile) {
            FileUtils.write(new File(initDataPath + f.getName() + ".json"), JsonUtils.toPrettyJson(f),
                    Charset.forName("UTF-8"));
        }
        return dao.save(sf);
    }

    @Transactional
    public void remove(String uid) {
        var e = dao.findById(uid).get();
        dao.delete(e);
    }

    public class InitFlowEvent extends ApplicationEvent {

        @Getter
        private List<Flow> flows;

        public InitFlowEvent(Object source, List<Flow> fs) {
            super(source);
            flows = fs;
        }

    }

}
