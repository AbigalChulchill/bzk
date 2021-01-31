package net.bzk.auth.service;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.bzk.auth.dao.BillDao;
import net.bzk.auth.dao.CosignDao;
import net.bzk.auth.dao.EndUserDao;
import net.bzk.auth.model.Account;
import net.bzk.auth.model.Bill;
import net.bzk.auth.model.Cosign;
import net.bzk.auth.model.EndUser;
import net.bzk.auth.model.UserBill;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.ex.BzkRuntimeException;

@Service
@Scope("prototype")
public class CosignCreator {

	@Inject
	private BillDao billDao;
	@Inject
	private UserBillService userBillService;
	@Inject
	private CosignDao dao;
	@Inject
	private EndUserDao endUserDao;

	private EndUser signUser;
	private Account signAccount;
	private Bill bill;

	private Cosign inObj;

	CosignCreator init(Account sa, Cosign ic) {
		inObj = ic;
		signAccount = sa;
		signUser = endUserDao.findByAccountOid(sa.getUid()).get();
		bill = billDao.findById(ic.getBillOid()).get();
		return this;
	}

	CosignCreator validate() {
		Optional<Cosign> cso = dao.findByBillOidAndEndUserOid(bill.getUid(), signUser.getUid());
		if (cso.isPresent())
			throw new BzkRuntimeException(
					"this sgin is exist bill:" + bill.getUid() + " signUser:" + signUser.getUid());
		return this;
	}

	Bundle cosign() {
		inObj.setBillOid(bill.getUid());
		inObj.setEndUserOid(signUser.getUid());
		inObj.setCreateAt(CommUtils.nowUtc0());
		inObj = dao.save(inObj);

		UserBill ub = userBillService.findByBillOidAndEndUserOid(bill.getUid(), signUser.getUid()).orElseGet(() -> {
			UserBill ans = new UserBill();
			ans.setBillOid(bill.getUid());
			ans.setEndUserOid(signUser.getUid());
			return ans;
		});
		ub.setCosignSupported(true);
		ub.setTransType(inObj.getTransType());
		ub.setHolded(inObj.isHolded());
		userBillService.save(ub);
		return new Bundle(ub, inObj);
	}

	@Data
	@AllArgsConstructor
	public static class Bundle {
		private UserBill userBill;
		private Cosign cosign;
	}

}
