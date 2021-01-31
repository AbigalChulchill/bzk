package net.bzk.auth.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import net.bzk.auth.model.Cosign;

public interface CosignDao extends CrudRepository<Cosign, Long> {

	Optional<Cosign> findByBillOidAndEndUserOid(String billOid, String enduserOid);
	
	@Query("SELECT c FROM Cosign c , Account a  ,EndUser e WHERE a.uid = e.accountOid and e.uid = c.endUserOid and a.uid = ?1 ")
	List< Cosign> listByAccountId(String accId);

}
