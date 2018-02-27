package com.example.springboot.common.mvc.exception;

public class MessageInvalidException extends RuntimeException {

	private static final long serialVersionUID = 8340565242548804574L;

	public MessageInvalidException() {
        super("");
    }
	
	public MessageInvalidException(String message) {
        super(message);
    }
}
