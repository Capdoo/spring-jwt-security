package com.example.demo.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserDTO {

	private Long id;
	private String name;
	private String username;
	private String email;
	private String password;
	private Set<String> roles = new HashSet<>();

}
