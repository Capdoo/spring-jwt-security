package com.example.demo.security.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.security.model.UsuarioModel;
import com.example.demo.security.repositories.UsuarioRepository;

@Service
//Para implementar rollbacks y evitar incoherencia : Concurrencia
@Transactional
public class UsuarioService {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	public Optional<UsuarioModel> getByNombreUsuario(String nombreUsuario){
		return usuarioRepository.findByNombreUsuario(nombreUsuario);
	}
	
	public boolean existsByNombreUsuario(String nombreUsuario) {
		return usuarioRepository.existsByNombreUsuario(nombreUsuario);
	}
	
	public boolean existsByEmail(String email) {
		return usuarioRepository.existsByEmail(email);
	}
	
	public void save(UsuarioModel usuarioModel) {
		usuarioRepository.save(usuarioModel);
	}
	
}











