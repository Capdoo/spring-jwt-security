package com.example.demo.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.security.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	Optional<UserEntity> findByUsername(String username);
	Optional<UserEntity> findByUsernameOrEmail(String username, String email);
	Optional<UserEntity> findByTokenPassword(String tokenPassword);
	boolean existsByUsername(String username);
	boolean existsByUsernameOrEmail(String username, String email);
	boolean existsByEmail(String email);

}
