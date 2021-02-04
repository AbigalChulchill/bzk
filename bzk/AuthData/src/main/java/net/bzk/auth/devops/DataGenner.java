package net.bzk.auth.devops;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import net.bzk.auth.dao.AccountDao;
import net.bzk.auth.dto.UserDto;
import net.bzk.auth.model.Account;
import net.bzk.auth.model.Account.Authority;
import net.bzk.auth.service.AccountService;
import net.bzk.infrastructure.CommUtils;
import net.bzk.infrastructure.JsonUtils;

@Service
@Profile({ "dev", "prod" })
public class DataGenner {

	
	@Inject
	private AccountService accountService;

	@Inject
	private AccountDao accountDao;

	private Map<String, Object> userOMap = new HashMap<>();
	private Map<DemoAccountE, Account> accMap = new HashMap<>();

	@Transactional
	@PostConstruct
	void init() {
		CommUtils.pl("DataGenner init");
		createAccounts();

		CommUtils.pl(JsonUtils.toJson(userOMap));
		CommUtils.pl(JsonUtils.toJson(accMap));

		CommUtils.pl("DataGenner init DONE");
	}

	private void createAccounts() {
		for (DemoAccountE dae : DemoAccountE.values()) {
			UserDto ud = new UserDto();
			ud.setUsername(dae.name());
			ud.setPassword("password");
			ud.setEmail(dae + "@dev.com");
			Account a = accountService.save(ud);
			accMap.put(dae, a);
			if (dae.hasAuthority(Authority.Admin)) {
				a.getAuthorities().add(Authority.Admin);
				accountDao.save(a);
			}

		}
	}



}
