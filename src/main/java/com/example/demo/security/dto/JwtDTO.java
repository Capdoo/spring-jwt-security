package com.example.demo.security.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
public class JwtDTO {

	private String token;
	public JwtDTO() {
	}
	public JwtDTO(String token) {
		super();
		this.token = token;

	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	
}
