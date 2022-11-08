package com.example.demo.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.security.enums.RoleName;
import com.example.demo.security.model.RoleModel;

@Repository
public interface RoleRepository extends JpaRepository<RoleModel, Integer>{
	
	Optional<RoleModel> findByRoleName(RoleName roleName);
	
	
}
