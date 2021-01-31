package net.bzk.auth.dao;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import net.bzk.auth.model.EndUser;

public interface EndUserDao extends CrudRepository<EndUser, String> {

	Optional<EndUser> findByAccountOid(String oid);
}
