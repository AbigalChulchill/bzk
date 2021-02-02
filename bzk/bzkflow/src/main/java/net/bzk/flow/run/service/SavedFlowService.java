package net.bzk.flow.run.service;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import net.bzk.flow.BzkFlowUtils;
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
		var ffs= FileUtils.listFiles(new File(initDataPath),new String[] {"json"}, false);
		ffs.forEach(this::importByFile);
	}
	
	private void importByFile(File f) {
		try {
			Flow flow= BzkFlowUtils.getFlowJsonMapper().readValue(f, Flow.class);
			save(flow);
		} catch (IOException e) {
			throw new BzkRuntimeException(e);
		}
		
	}
	
	@Transactional
	public SavedFlow save(Flow f) {
		var sfo= dao.findById(f.getUid());
		var sf= sfo.orElse(SavedFlow.gen(f));
		sf.setModel(f);
		return dao.save(sf);
	}

	@Transactional
	public void remove(String uid) {
		var e = dao.findById(uid).get();
		dao.delete(e);		
	}
	
	

}
