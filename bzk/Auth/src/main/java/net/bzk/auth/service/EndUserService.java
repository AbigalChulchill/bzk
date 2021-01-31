package net.bzk.auth.service;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.inject.Inject;
import javax.inject.Provider;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.bzk.auth.dao.AccountDao;
import net.bzk.auth.dao.EndUserDao;
import net.bzk.auth.model.Account;
import net.bzk.auth.model.EndUser;
import net.bzk.auth.model.EndUserEachMap;
import net.bzk.auth.model.Account.Authority;
import net.bzk.auth.model.EndUserEachMap.EndUserEachMapId;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
public class EndUserService {

	@Inject
	private EndUserDao dao;
	@Inject
	private AccountDao accountDao;
	@Inject
	private DozerBeanMapper mapper;

	@Transactional
	public BecomeEndUserBundle becomeEndUser(Account a, Consumer<EndUser> euc) {
		Optional<EndUser> euo = findByAccountOid(a.getUid());
		if (euo.isPresent())
			throw new BzkRuntimeException("Already Exist EndUser Account:" + a.getUid() + " user:" + a.getUsername());
		EndUser eu = new EndUser();
		eu.setAccountOid(a.getUid());
		euc.accept(eu);
		dao.save(eu);
		a.getAuthorities().add(Authority.EndUser);
		a = accountDao.save(a);



		return new BecomeEndUserBundle(eu, a);
	}

	@Data
	@AllArgsConstructor
	public static class BecomeEndUserBundle {
		private EndUser endUser;
		private Account account;
	}

	@Transactional(readOnly = true)
	public Optional<EndUser> findByAccountOid(String aoid) {
		return dao.findByAccountOid(aoid);
	}

	@Transactional
	public EndUser putEndUser(Account a, EndUser eu) {
		EndUser ou = dao.findById(eu.getUid()).get();
		if(!StringUtils.equals(ou.getAccountOid(), a.getUid())) throw new BzkRuntimeException("the account not owner");
		mapper.map(eu, ou);
		return dao.save(ou);
	}

}
