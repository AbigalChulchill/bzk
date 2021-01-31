package net.bzk.auth.devops;

import lombok.Getter;
import net.bzk.auth.model.Account.Authority;

public enum DemoAccountE {

	Admin1(Authority.Admin), 
	domidev(Authority.Admin); 

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
