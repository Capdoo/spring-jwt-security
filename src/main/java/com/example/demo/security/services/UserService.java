package com.example.demo.security.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.security.model.UserModel;
import com.example.demo.security.repositories.UserRepository;

@Service
//Para implementar rollbacks y evitar incoherencia : Concurrencia
@Transactional
public class UserService {

	@Autowired
	UserRepository userRepository;
	
	public Optional<UserModel> getByUsername(String username){
		return userRepository.findByUsername(username);
	}

	public Optional<UserModel> getByUsernameOrEmail(String usernameOrEmail){
		return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
	}

	public Optional<UserModel> getByTokenPassword(String tokenPassword){
		return userRepository.findByTokenPassword(tokenPassword);
	}

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	public boolean existsByUsernameOrEmail(String usernameOrEmail) {
		return userRepository.existsByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
	}
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	public void save(UserModel userModel) {
		userRepository.save(userModel);
	}
}











