package net.bzk.flow.run.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import net.bzk.flow.model.Flow;
import net.bzk.flow.run.dao.RunFlowPoolDao;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
public class RunFlowService {

	@Inject
	private RunFlowPoolDao runPoolDao;

	public void register(Flow f) {
		boolean b = runPoolDao.create(f);
		if (!b)
			throw new BzkRuntimeException("exist :" + f);
	}
}
