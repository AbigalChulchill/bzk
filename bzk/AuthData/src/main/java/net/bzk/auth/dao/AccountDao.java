package net.bzk.auth.dao;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.bzk.auth.model.Account;

@Repository
public interface AccountDao extends CrudRepository<Account, String> {
	Optional<Account> findByUsername(String username);
}