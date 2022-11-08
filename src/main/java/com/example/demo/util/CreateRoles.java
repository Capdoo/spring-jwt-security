package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.security.enums.RoleName;
import com.example.demo.security.model.RoleModel;
import com.example.demo.security.services.RoleService;

@Component
public class CreateRoles implements CommandLineRunner {

	
	@Autowired
	RoleService roleService;
	
	@Override
	public void run(String... args) throws Exception {

		RoleModel rolAdmin = new RoleModel(RoleName.ROLE_ADMIN);
		
		RoleModel rolUser = new RoleModel(RoleName.ROLE_USER);
		
		roleService.save(rolAdmin);
		roleService.save(rolUser);

	}
	 
	
	
	
	
}
