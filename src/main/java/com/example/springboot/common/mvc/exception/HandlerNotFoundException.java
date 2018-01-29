package com.example.springboot.common.mvc.exception;

public class HandlerNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 632771414655162434L;

	public HandlerNotFoundException() {
        super("");
    }
	
	public HandlerNotFoundException(String message) {
        super(message);
    }
}
