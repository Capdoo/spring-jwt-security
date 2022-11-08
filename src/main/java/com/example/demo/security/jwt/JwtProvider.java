package com.example.demo.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.demo.security.model.MainUserModel;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

//Generar token : validación en la creación

@Component
public class JwtProvider {

	private final static Logger logger = LoggerFactory.getLogger(JwtProvider.class); 

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private int expiration;
	
	public String generateToken(Authentication authentication) {
		MainUserModel usuarioPrincipal = (MainUserModel) authentication.getPrincipal();
		
		return Jwts.builder().setSubject(usuarioPrincipal.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + expiration*1000))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	//Obtiene nombreUsuario desde token
	public String getNombreUsuarioFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}
	
	
	public boolean validateToken(String token) {
		try {
			
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
			
		} catch (MalformedJwtException e) {
			logger.error("Token mal formado");
			
		}catch (UnsupportedJwtException e) {
			logger.error("Token no soportado");
			
		}catch (ExpiredJwtException e) {
			logger.error("Token expirado");
			
		}catch (IllegalArgumentException e) {
			logger.error("Token vacio");
			
		}catch (SignatureException e) {
			logger.error("Fallo en la firma");
			
		}
		return false;
	}
	
	
}










