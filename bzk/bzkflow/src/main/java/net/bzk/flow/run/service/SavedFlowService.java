package net.bzk.flow.run.service;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import net.bzk.flow.model.Flow;
import net.bzk.flow.model.SavedFlow;
import net.bzk.flow.run.dao.SavedFlowDao;

@Service
public class SavedFlowService {

	@Inject
	private SavedFlowDao dao;
	
	@Transactional
	public SavedFlow save(Flow f) {
		var sfo= dao.findById(f.getUid());
		var sf= sfo.orElse(new SavedFlow());
		sf.setModel(f);
		return dao.save(sf);
	}

	@Transactional
	public void remove(String uid) {
		var e = dao.findById(uid).get();
		dao.delete(e);		
	}
	
	

}
