package net.bzk.auth.service;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import net.bzk.auth.dao.AccountDao;
import net.bzk.auth.dto.UserDto;
import net.bzk.auth.model.Account;
import net.bzk.auth.security.UserPrincipal;

@Service
public class AccountService implements UserDetailsService {

	@Inject
	private AccountDao dao;
	@Inject
	private EndUserService endUserService;
	@Inject
	private PasswordEncoder bcryptEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Account> userO = dao.findByUsername(username);
		if (!userO.isPresent()) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		Account user = userO.get();
		return UserPrincipal.create(user);
	}

	public Optional<Account> findByUserName(String userName) {
		return dao.findByUsername(userName);
	}

	public Account save(UserDto user) {
		Account newUser = new Account();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
		newUser.setRefCode(user.getRefCode());
		newUser.setEmail(user.getEmail());
		newUser = dao.save(newUser);
		if (user.isTobeEndUser()) {
			endUserService.becomeEndUser(newUser, eu -> {
			});
		}
		return newUser;
	}
}