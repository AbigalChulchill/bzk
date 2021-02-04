package net.bzk.auth.model;

import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import net.bzk.auth.JpaConstant;

@SuppressWarnings("serial")
@Data
@Entity
@Table
public class Account implements org.springframework.security.core.userdetails.UserDetails {

	@Id
	@Column(nullable = false)
	private String uid;
	@Column(nullable = false, unique = true)
	private String username;
	@Column(nullable = false, unique = true)
	private String email;
	@Column(nullable = false)
	@JsonIgnore
	private String password;
	@Column(nullable = false)
	private boolean accountNonExpired;
	@Column(nullable = false)
	private boolean accountNonLocked;
	@Column(nullable = false)
	private boolean credentialsNonExpired;
	@Column(nullable = false)
	private boolean enabled;
	@Column(nullable = true)
	private String refCode;

	@Column(nullable = true)
	@Convert(converter = AuthorityListConvert.class)
	private AuthorityList authorities = new AuthorityList();

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_MEDIUM_TEXT)
	private AuthProvider provider = AuthProvider.local;
	@Column(nullable = true, columnDefinition = JpaConstant.COLUMN_DEFIN_MEDIUM_TEXT)
	private String providerId;

	public Account() {
		uid = RandomStringUtils.randomAlphanumeric(JpaConstant.COLUMN_DEFIN_UID_SIZE);
	}

	public String getEmail() {
		return email;
	}
	
	public static class AuthorityListConvert extends JsonPojoConverter<AuthorityList> {
		@Override
		public Class<AuthorityList> getTClass() {
			return AuthorityList.class;
		}
	}

	public static class AuthorityList extends HashSet<Authority> {

		public AuthorityList() {
		}
	}

	public static enum Authority implements GrantedAuthority {
		Admin;

		@Override
		public String getAuthority() {
			return this.toString();
		}
	}

	public static enum AuthProvider {
		local, facebook, google, github
	}

}