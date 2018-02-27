package com.example.springboot.common.dispatcher.exception;

public class ParameterTypeInvalidException extends RuntimeException {

	private static final long serialVersionUID = -5479830857981218130L;

	public ParameterTypeInvalidException() {
        super("");
    }
	
	public ParameterTypeInvalidException(String message) {
        super(message);
    }
}
