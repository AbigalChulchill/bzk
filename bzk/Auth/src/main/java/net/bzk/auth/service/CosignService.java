package net.bzk.auth.service;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import net.bzk.auth.dao.CosignDao;
import net.bzk.auth.model.Account;
import net.bzk.auth.model.Cosign;

@Service
public class CosignService {

	@Inject
	private CosignDao dao;
	@Inject
	private Provider<CosignCreator> cosignCreatorProvider;

	@Transactional
	public CosignCreator.Bundle create(Account sa, Cosign ic) {
		return cosignCreatorProvider.get().init(sa, ic).validate().cosign();
	}

}
