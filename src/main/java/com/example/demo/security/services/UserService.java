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
	
	public Optional<UserModel> getByNombreUsuario(String nombreUsuario){
		return userRepository.findByNombreUsuario(nombreUsuario);
	}
	
	public boolean existsByNombreUsuario(String nombreUsuario) {
		return userRepository.existsByNombreUsuario(nombreUsuario);
	}
	
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public void save(UserModel userModel) {
		userRepository.save(userModel);
	}
	
}











