package com.example.demo.security.controllers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.MensajeDTO;
import com.example.demo.security.dto.JwtDTO;
import com.example.demo.security.dto.LoginUsuarioDTO;
import com.example.demo.security.dto.NuevoUsuarioDTO;
import com.example.demo.security.enums.RolNombre;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.model.RolModel;
import com.example.demo.security.model.UsuarioModel;
import com.example.demo.security.services.RolService;
import com.example.demo.security.services.UsuarioService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RolService rolService;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@PostMapping("/nuevo")
	public ResponseEntity<Object> nuevo(@RequestBody NuevoUsuarioDTO nuevoUsuarioDTO, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			return new ResponseEntity(new MensajeDTO("Campos mal colocados"), HttpStatus.BAD_REQUEST);
		}
		
		if (usuarioService.existsByNombreUsuario(nuevoUsuarioDTO.getNombreUsuario())) {
			return new ResponseEntity(new MensajeDTO("El nombre de usuario ya existe"), HttpStatus.BAD_REQUEST);
	
		}
		if (usuarioService.existsByEmail(nuevoUsuarioDTO.getEmail())) {
			return new ResponseEntity(new MensajeDTO("El email ya existe"), HttpStatus.BAD_REQUEST);
	
		}
		
		UsuarioModel usuarioModel = new UsuarioModel(
										nuevoUsuarioDTO.getNombre(),
										nuevoUsuarioDTO.getNombreUsuario(),
										nuevoUsuarioDTO.getEmail(),
										passwordEncoder.encode(nuevoUsuarioDTO.getPassword())
									);
		
		Set<RolModel> roles = new HashSet<>();
		roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
		if (nuevoUsuarioDTO.getRoles().contains("admin")) {
			roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
		}
		
		usuarioModel.setRoles(roles);
		usuarioService.save(usuarioModel);
		
		return new ResponseEntity(new MensajeDTO("Usuario guardado"), HttpStatus.CREATED);
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginUsuarioDTO loginUsuarioDTO, BindingResult bindingResult){
		if (bindingResult.hasErrors()) {
			return new ResponseEntity(new MensajeDTO("Campos mal colocados"), HttpStatus.BAD_REQUEST);
		}
		
		Authentication authentication = authenticationManager.authenticate(
							new UsernamePasswordAuthenticationToken(
														loginUsuarioDTO.getNombreUsuario(), 
														loginUsuarioDTO.getPassword()
														)
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		
		JwtDTO jwtDTO = new JwtDTO(jwt, userDetails.getUsername(), userDetails.getAuthorities());
		
		return new ResponseEntity(jwtDTO, HttpStatus.OK);

	}
	
	
}











