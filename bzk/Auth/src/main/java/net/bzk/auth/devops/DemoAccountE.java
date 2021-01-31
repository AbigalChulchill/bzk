package net.bzk.auth.devops;

import lombok.Getter;
import net.bzk.auth.model.Account.Authority;

public enum DemoAccountE {

	Admin1(Authority.Admin), EndUser1(Authority.EndUser), Payer1(Authority.Payer),
	domidev(Authority.Admin, Authority.EndUser, Authority.Payer);

	@Getter
	private Authority[] authoritys;

	private DemoAccountE(Authority... a) {
		authoritys = a;
	}

	public boolean hasAuthority(Authority a) {
		for (Authority _a : Authority.values()) {
			if (_a == a)
				return true;
		}
		return false;
	}

}
