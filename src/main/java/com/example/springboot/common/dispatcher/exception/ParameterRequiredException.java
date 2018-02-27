package com.example.springboot.common.dispatcher.exception;

public class ParameterRequiredException extends RuntimeException {

	private static final long serialVersionUID = -5479830857981218130L;

	public ParameterRequiredException() {
        super("");
    }
	
	public ParameterRequiredException(String message) {
        super(message);
    }
}
