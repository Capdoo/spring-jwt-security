package com.example.demo.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "Name can not be empty")
	@Column(name="name", nullable = false)
	private String name;

	@NotEmpty(message = "Username can not be empty")
	@Column(unique = true, nullable = false)
	private String username;

	@NotEmpty(message = "Email can not be empty")
	@Email(message = "Email is not valid")
	@Column(unique = true, nullable = false)
	private String email;

	@NotEmpty(message = "Password can not be empty")
	private String password;

	@Column(name="token_password")
	private String tokenPassword;

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="user_roles", joinColumns = @JoinColumn(name = "id_user"), inverseJoinColumns = @JoinColumn(name = "id_role"))
	private Set<RoleEntity> roles = new HashSet<>();

	private String state;

}

























