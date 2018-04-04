package com.example.springboot.common.dispatcher.exception;

public class ParameterInvalidException extends RuntimeException {

	private static final long serialVersionUID = -5479830857981218130L;

	public ParameterInvalidException() {
		super();
	}

	public ParameterInvalidException( Throwable cause ) {
		super( cause );
	}
}
