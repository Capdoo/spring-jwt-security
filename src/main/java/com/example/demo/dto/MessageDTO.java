package com.example.demo.dto;

public class MessageDTO {

	public MessageDTO(String mensaje) {
		super();
		this.mensaje = mensaje;
	}
	
	private String mensaje;

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	
}