package com.example.demo.security.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.security.enums.RoleName;
import com.example.demo.security.model.RoleModel;
import com.example.demo.security.repositories.RoleRepository;

@Service
@Transactional

public class RoleService {

	@Autowired
	RoleRepository roleRepository;
	
	public Optional<RoleModel> getByRoleName (RoleName roleName){
		return roleRepository.findByRoleName(roleName);
	}
	public void save(RoleModel roleModel) {
		roleRepository.save(roleModel);
	}
}
