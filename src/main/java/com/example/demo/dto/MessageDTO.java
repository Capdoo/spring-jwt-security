package com.example.demo.dto;

public class MessageDTO {

	public MessageDTO(String message) {
		super();
		this.message = message;
	}
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
