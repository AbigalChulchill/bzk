package net.bzk.auth.dto;

import lombok.Data;

@Data
public class UserDto {
	private String username;
	private String password;
	private String email;
	private String refCode;
	private boolean tobeEndUser;

}