package com.example.demo.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.security.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer>{

	Optional<UserModel> findByNombreUsuario(String nombreUsuario);
	boolean existsByNombreUsuario(String nombreUsuario);
	boolean existsByEmail(String email);
	
}
