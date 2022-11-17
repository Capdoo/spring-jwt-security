package com.example.demo.security.controllers;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.MessageDTO;
import com.example.demo.security.dto.JwtDTO;
import com.example.demo.security.dto.LoginUserDTO;
import com.example.demo.security.dto.NewUserDTO;
import com.example.demo.security.enums.RoleName;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.model.RoleModel;
import com.example.demo.security.model.UserModel;
import com.example.demo.security.services.RoleService;
import com.example.demo.security.services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserService userService;
	@Autowired
	RoleService roleService;
	@Autowired
	JwtProvider jwtProvider;

	@PostMapping("/register")
	public ResponseEntity<Object> nuevo(@RequestBody NewUserDTO newUserDTO, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			return new ResponseEntity(new MessageDTO("Campos mal colocados"), HttpStatus.BAD_REQUEST);
		}
		
		if (userService.existsByUsername(newUserDTO.getNombreUsuario())) {
			return new ResponseEntity(new MessageDTO("El nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
	
		}
		if (userService.existsByEmail(newUserDTO.getEmail())) {
			return new ResponseEntity(new MessageDTO("El email ya existe"), HttpStatus.BAD_REQUEST);
	
		}
		
		UserModel userModel = new UserModel(
										newUserDTO.getNombre(),
										newUserDTO.getNombreUsuario(),
										newUserDTO.getEmail(),
										passwordEncoder.encode(newUserDTO.getPassword())
									);
		
		Set<RoleModel> roles = new HashSet<>();
		roles.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
		if (newUserDTO.getRoles().contains("admin")) {
			roles.add(roleService.getByRoleName(RoleName.ROLE_ADMIN).get());
		}
		
		userModel.setRoles(roles);
		userService.save(userModel);
		
		return new ResponseEntity(new MessageDTO("Usuario guardado"), HttpStatus.CREATED);
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginUserDTO loginUserDTO, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			return new ResponseEntity(new MessageDTO("Campos mal colocados"), HttpStatus.BAD_REQUEST);
		}
		
		if(!(userService.existsByUsername(loginUserDTO.getUsername()))) {
			return new ResponseEntity(new MessageDTO("Campos mal colocados"), HttpStatus.BAD_REQUEST);
		}
		
        return Autenticacion(loginUserDTO.getUsername(), loginUserDTO.getPassword());
		
	}
	
	public ResponseEntity<Object> Autenticacion(String username, String password) {
		
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String jwt = jwtProvider.generateToken(authentication);
	        JwtDTO jwtDto = new JwtDTO(jwt);
	        return new ResponseEntity(jwtDto, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity(new MessageDTO("Campos mal colocados"), HttpStatus.BAD_REQUEST);
		}

	}
	
	
}











