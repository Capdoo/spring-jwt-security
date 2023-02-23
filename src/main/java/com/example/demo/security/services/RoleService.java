package com.example.demo.security.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.security.enums.RoleName;
import com.example.demo.security.model.RoleEntity;
import com.example.demo.security.repositories.RoleRepository;

@Service
@Transactional

public class RoleService {
	@Autowired
	RoleRepository roleRepository;
	
	public RoleEntity getByRoleName (RoleName roleName){
		return roleRepository.findByRoleName(roleName).orElse(null);
	}
	public void save(RoleEntity roleEntity) {
		roleRepository.save(roleEntity);
	}
}
