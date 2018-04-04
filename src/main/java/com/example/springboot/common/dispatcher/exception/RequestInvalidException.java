package com.example.springboot.common.dispatcher.exception;

public class RequestInvalidException extends RuntimeException {

	private static final long serialVersionUID = 8340565242548804574L;

	public RequestInvalidException() {
        super("");
    }
	
	public RequestInvalidException(String message) {
        super(message);
    }

	public RequestInvalidException( String message, Throwable cause ) {
		super( message, cause );
	}

	public RequestInvalidException( Throwable cause ) {
		super( cause );
	}
}
