package net.bzk.flow.run.service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import net.bzk.flow.BzkFlowUtils;
import net.bzk.flow.model.Action.SubFlowAction;
import net.bzk.flow.model.Flow;
import net.bzk.flow.model.SavedFlow;
import net.bzk.flow.run.dao.SavedFlowDao;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
public class SavedFlowService {

	public static final String initDataPath = "/bzk/model/flow/";

	@Inject
	private SavedFlowDao dao;

	@PostConstruct
	public void loadInitData() {

		var dir = new File(initDataPath);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		var ffs = FileUtils.listFiles(dir, new String[] { "json" }, false);
		ffs.forEach(this::importByFile);
	}

	private void importByFile(File f) {
		try {
			Flow flow = BzkFlowUtils.getFlowJsonMapper().readValue(f, Flow.class);
			save(flow);
		} catch (IOException e) {
			throw new BzkRuntimeException(e);
		}
	}

	public Collection<SavedFlow> listDepends(String uid) {
		Set<SavedFlow> ans = new HashSet<>();
		var tar = dao.findById(uid).get();
		recListDepends(ans, tar);
		return ans;
	}

	private void recListDepends(Set<SavedFlow> ans, SavedFlow sf) {
		if (ans.stream().anyMatch(_f -> StringUtils.equals(_f.getUid(), sf.getUid()))) {
			return;
		}
		ans.add(sf);
		Set<SubFlowAction> sas = sf.getModel().listAllActions().stream().filter(a -> a instanceof SubFlowAction)
				.map(a -> (SubFlowAction) a).collect(Collectors.toSet());
		Set<SavedFlow> csf = sas.stream().map(a -> dao.findById(a.getFlowUid()).get()).collect(Collectors.toSet());
		for (SavedFlow sfchild : csf) {
			recListDepends(ans, sfchild);
		}

	}

	@Transactional
	public SavedFlow save(Flow f) {
		var sfo = dao.findById(f.getUid());
		var sf = sfo.orElse(SavedFlow.gen(f));
		sf.setModel(f);
		return dao.save(sf);
	}

	@Transactional
	public void remove(String uid) {
		var e = dao.findById(uid).get();
		dao.delete(e);
	}

}
