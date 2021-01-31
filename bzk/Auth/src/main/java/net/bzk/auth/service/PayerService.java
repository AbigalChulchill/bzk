package net.bzk.auth.service;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import net.bzk.auth.dao.AccountDao;
import net.bzk.auth.dao.BillDao;
import net.bzk.auth.dao.PayerDao;
import net.bzk.auth.model.Account;
import net.bzk.auth.model.Bill;
import net.bzk.auth.model.Payer;
import net.bzk.auth.model.Account.Authority;
import net.bzk.auth.model.Payer.State;

@Service
public class PayerService {

	@Inject
	private AccountDao accountDao;
	@Inject
	private PayerDao dao;
	@Inject
	private DozerBeanMapper mapper;
	@Inject
	private BillDao billDao;

	@Transactional
	public Payer create(Payer p) {
		return dao.save(p);
	}

	@Transactional
	public Payer update(String uid, Payer p) {
		Payer _p = dao.findById(uid).get();
		mapper.map(p, _p);
		return dao.save(_p);
	}

	@Transactional
	public Bill createBill(String payerUid, Bill b) {
		Payer p = dao.findById(payerUid).get();
		b.setPayerOid(p.getUid());
		return billDao.save(b);
	}
	
	@Transactional
	public Payer getByUserName(String username) {
		Account a= accountDao.findByUsername(username).get();
		return dao.findByAccountOid(a.getUid()).get();
	}

	@Transactional
	public Payer become(String accUid) {
		Account a = accountDao.findById(accUid).get();
		a.getAuthorities().add(Authority.Payer);
		accountDao.save(a);
		Payer p = new Payer();
		p.setName(a.getUsername());
		p.setAccountOid(a.getUid());
		p.setState(State.Zero);
		return dao.save(p);
	}

}
