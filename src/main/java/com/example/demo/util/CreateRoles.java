package com.example.demo.util;

import com.example.demo.security.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.example.demo.security.enums.RoleName;
import com.example.demo.security.model.RoleEntity;
import com.example.demo.security.services.RoleService;
import org.springframework.stereotype.Component;

@Component
public class CreateRoles implements CommandLineRunner {

	@Autowired
	RoleService roleService;

	@Override
	public void run(String... args) throws Exception {

		RoleEntity rolAdmin = new RoleEntity(RoleName.ROLE_ADMIN);
		RoleEntity rolUser = new RoleEntity(RoleName.ROLE_USER);

		if (roleService.getByRoleName(RoleName.ROLE_ADMIN) == null){
			roleService.save(rolAdmin);
		}

		if (roleService.getByRoleName(RoleName.ROLE_USER) == null){
			roleService.save(rolUser);
		}

	}
	 
	
	
	
	
}
