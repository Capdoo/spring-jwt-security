package com.example.demo.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.security.model.UsuarioModel;
import com.example.demo.security.model.UsuarioPrincipalModel;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	UsuarioService usuarioService;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		UsuarioModel usuarioModel = usuarioService.getByNombreUsuario(username).get();
		
		return UsuarioPrincipalModel.build(usuarioModel);
	}

	
	
}
