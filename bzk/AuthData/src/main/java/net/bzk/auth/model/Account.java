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
@Table(name = "account")
public class Account implements org.springframework.security.core.userdetails.UserDetails {

	@Id
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	private String uid;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_MEDIUM_TEXT, unique = true)
	private String username;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_MEDIUM_TEXT, unique = true)
	private String email;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_LARGE_TEXT)
	@JsonIgnore
	private String password;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_BOOLEAN)
	private boolean accountNonExpired;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_BOOLEAN)
	private boolean accountNonLocked;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_BOOLEAN)
	private boolean credentialsNonExpired;
	@Column(nullable = false, columnDefinition = JpaConstant.COLUMN_DEFIN_BOOLEAN)
	private boolean enabled;
	@Column(nullable = true, columnDefinition = JpaConstant.COLUMN_DEFIN_UID)
	private String refCode;

	@Column(nullable = true, columnDefinition = JpaConstant.COLUMN_DEFIN_XLARGE_TEXT)
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