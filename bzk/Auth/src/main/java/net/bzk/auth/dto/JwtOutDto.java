package net.bzk.auth.dto;

import java.io.Serializable;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

public class JwtOutDto implements Serializable {
	private static final long serialVersionUID = -8091879091924046844L;
	@Getter
	private final String jwttoken;
	@Getter
	private final Collection<? extends GrantedAuthority> authorities ;

	public JwtOutDto(String jwttoken, Collection<? extends GrantedAuthority> as) {
		this.jwttoken = jwttoken;
		authorities = as;
	}

}
