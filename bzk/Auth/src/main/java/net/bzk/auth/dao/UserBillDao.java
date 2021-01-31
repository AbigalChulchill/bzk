package net.bzk.auth.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import net.bzk.auth.model.UserBill;

public interface UserBillDao extends CrudRepository<UserBill, String> {

	Optional<UserBill> findByBillOidAndEndUserOid(String billOid, String endUserOid);

}
