package com.example.demo.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.security.enums.RolNombre;
import com.example.demo.security.model.RolModel;

@Repository
public interface RolRepository extends JpaRepository<RolModel, Integer>{
	
	Optional<RolModel> findByRolNombre(RolNombre rolNombre);
	
	
}
