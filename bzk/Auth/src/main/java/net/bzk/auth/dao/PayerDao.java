package net.bzk.auth.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import net.bzk.auth.model.Payer;
import net.bzk.auth.model.Payer.State;

public interface PayerDao extends CrudRepository<Payer, String> {

	List<Payer> findAllByState(State state, Pageable pageable);

	Optional<Payer> findByAccountOid(String accountOid);

}
