package net.bzk.flow.run.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.model.Action.SubFlowAction;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.Job;
import net.bzk.flow.run.dao.JobsDao;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
public class JobsService {

	public static final String initDataPath = "/bzk/model/flow/";

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
		var ffs = FileUtils.listFiles(dir, new String[] { "json" }, false);
		ffs.forEach(this::importByFile);
		CommUtils.pl("mapper:" + mapper);
	}

	@Transactional
	private void importByFile(File f) {
		try {
			Flow flow = BzkFlowUtils.getFlowJsonMapper().readValue(f, Flow.class);
			save(flow);
		} catch (IOException e) {
			throw new BzkRuntimeException(e);
		}
	}

	public Collection<Job> listDepends(String uid) {
		Set<Job> ans = new HashSet<>();
		var tar = dao.findById(uid).get();
		recListDepends(ans, tar);
		return ans;
	}

	private void recListDepends(Set<Job> ans, Job sf) {
		if (ans.stream().anyMatch(_f -> StringUtils.equals(_f.getUid(), sf.getUid()))) {
			return;
		}
		ans.add(sf);
		Set<SubFlowAction> sas = sf.getFlow().listAllActions().stream().filter(a -> a instanceof SubFlowAction)
				.map(a -> (SubFlowAction) a).collect(Collectors.toSet());
		Set<Job> csf = sas.stream().map(a -> dao.findById(a.getFlowUid()).get()).collect(Collectors.toSet());
		for (Job sfchild : csf) {
			recListDepends(ans, sfchild);
		}

	}

	@Transactional
	public Job save(Flow f) {
		var sfo = dao.findById(f.getUid());
		Job sf = sfo.orElse(Job.gen(f));
		String json = JsonUtils.toJson(f);
		sf.setModel(json);
		return dao.save(sf);
	}

	@Transactional
	public void remove(String uid) {
		var e = dao.findById(uid).get();
		dao.delete(e);
	}

}
