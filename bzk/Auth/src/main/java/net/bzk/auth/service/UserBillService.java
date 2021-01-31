package net.bzk.auth.service;

import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.dozer.DozerBeanMapper;
import org.springframework.stereotype.Service;

import net.bzk.auth.dao.UserBillDao;
import net.bzk.auth.model.UserBill;

@Service
public class UserBillService {

	@Inject
	private UserBillDao dao;
	@Inject
	private DozerBeanMapper mapper;

	@Transactional
	public UserBill save(UserBill ub) {
		Optional<UserBill> ubo = dao.findByBillOidAndEndUserOid(ub.getBillOid(), ub.getEndUserOid());
		if (!ubo.isPresent()) {
			return dao.save(ub);
		}
		UserBill _ub = ubo.get();
		mapper.map(ub, _ub);
		return dao.save(_ub);
	}

	public Optional<UserBill> findByBillOidAndEndUserOid(String billOid, String endUserOid) {
		return dao.findByBillOidAndEndUserOid(billOid, endUserOid);
	}

}
