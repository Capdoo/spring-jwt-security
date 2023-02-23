package com.example.demo.security.controllers;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import com.example.demo.security.services.UserService;
import com.example.demo.util.ErrorMessageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.example.demo.security.dto.UserDTO;
import com.example.demo.security.jwt.JwtProvider;
import com.example.demo.security.model.UserEntity;
import org.springframework.web.server.ResponseStatusException;

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
	JwtProvider jwtProvider;

	@PostMapping("/register")
	public ResponseEntity<Object> nuevo(@RequestBody UserDTO userDTO, BindingResult bindingResult) throws JsonProcessingException {
		if (bindingResult.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(bindingResult));
		}
		if (userService.existsByUsername(userDTO.getUsername())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");

		}
		if (userService.existsByEmail(userDTO.getEmail())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
		}

		userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
		UserEntity userCreate = userService.createUser(userDTO);

		return ResponseEntity.status(HttpStatus.CREATED).body(userCreate);
	}
	
	@PostMapping("/login")
	public ResponseEntity<Object> login(@RequestBody LoginUserDTO loginUserDTO, BindingResult bindingResult) throws JsonProcessingException {
		if (bindingResult.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(bindingResult));
		}
		
		if(!(userService.existsByUsernameOrEmail(loginUserDTO.getUsernameOrEmail()))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Username does not exists");
		}
		
        return Autentication(loginUserDTO.getUsernameOrEmail(), loginUserDTO.getPassword());
		
	}
	
	public ResponseEntity<Object> Autentication(String username, String password) {
		
		try {
			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        String jwt = jwtProvider.generateToken(authentication);
	        JwtDTO jwtDto = new JwtDTO(jwt);
			return ResponseEntity.status(HttpStatus.OK).body(jwtDto);

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong fields");
		}
	}

	@PostMapping("/refresh")
	public ResponseEntity<Object> refreshToken(@RequestBody JwtDTO jwtDTO) throws ParseException {
		try {
			String token = jwtProvider.refreshToken(jwtDTO);
			JwtDTO jwt = new JwtDTO(token);
			return new ResponseEntity<Object>(jwt, HttpStatus.OK);

		}catch (Exception e){
			return new ResponseEntity<Object>(new MessageDTO(e.getMessage()), HttpStatus.OK);
		}
	}

	private String formatMessage(BindingResult bindingResult) throws JsonProcessingException {
		List<Map<String, String>> errors = bindingResult.getFieldErrors().stream()
				.map(err -> {
					Map<String, String> error = new HashMap<>();
					error.put(err.getField(), err.getDefaultMessage());
					return error;
				}).collect(Collectors.toList());
		ErrorMessageUtil errorMessage = ErrorMessageUtil.builder()
				.code("01")
				.messages(errors).build();

		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = "";
		try {
			jsonString = objectMapper.writeValueAsString(errorMessage);

		}catch (JsonProcessingException e){
			e.printStackTrace();
		}
		return jsonString;
	}
	
	
}











