package com.example.demo.security.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import com.example.demo.security.dto.UserDTO;
import com.example.demo.security.enums.RoleName;
import com.example.demo.security.model.RoleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.security.model.UserEntity;
import com.example.demo.security.repositories.UserRepository;

@Service
//Para implementar rollbacks y evitar incoherencia : Concurrencia
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleService roleService;

	@Override
	public List<UserEntity> listAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public UserEntity createUser(UserDTO userDTO) {

		UserEntity userEntity = UserEntity.builder()
				.name(userDTO.getName())
				.username(userDTO.getUsername())
				.email(userDTO.getEmail())
				.password(userDTO.getPassword())
				.state("CREATED")
				.build();

		Set<RoleEntity> roles = new HashSet<>();
		roles.add(roleService.getByRoleName(RoleName.ROLE_USER));
		if (userDTO.getRoles().contains("admin")) {
			roles.add(roleService.getByRoleName(RoleName.ROLE_ADMIN));
		}
		userEntity.setRoles(roles);
		return userRepository.save(userEntity);
	}

	@Override
	public UserEntity readUser(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public UserEntity updateUser(UserDTO userDTO) {
		UserEntity userEntity = readUser(userDTO.getId());
		if (userEntity == null) return null;

		userEntity.setName(userDTO.getName());
		userEntity.setEmail(userDTO.getEmail());

		return userRepository.save(userEntity);
	}

	@Override
	public UserEntity deleteUser(UserDTO userDTO) {
		UserEntity userEntity = readUser(userDTO.getId());
		if (userEntity == null) return null;

		userEntity.setState("DELETED");

		return userRepository.save(userEntity);
	}

	@Override
	public Boolean existsById(Long id) {
		return userRepository.existsById(id);
	}
	@Override
	public UserEntity readByUsernameOrEmail(String usernameOrEmail){
		return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElse(null);
	}
	@Override
	public UserEntity readByTokenPassword(String tokenPassword){
		return userRepository.findByTokenPassword(tokenPassword).orElse(null);
	}


	@Override
	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	@Override
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	@Override
	public Boolean existsByUsernameOrEmail(String usernameOrEmail) { return userRepository.existsByUsernameOrEmail(usernameOrEmail, usernameOrEmail); }
}











