package com.example.springboot.common.exception;

public class BaseException extends Exception {

	private static final long serialVersionUID = -801332163959732371L;
	
	private Object target;
	
	public BaseException() {}
	
	public BaseException( String message, Throwable cause, Object target ) {
		super( message, cause );
		this.target = target;
	}

	public BaseException( String message, Throwable cause ) {
		super( message, cause );
	}

	public BaseException( String message ) {
		super( message );
	}
	
	public BaseException( String message, Object target ) {
		super( message );
		this.target = target;
	}

	public BaseException( Throwable cause ) {
		super( cause.getMessage(), cause );
	}

	public BaseException( Throwable cause, Object target) {
		super( cause.getMessage(), cause );
		this.target = target;
	}

	public Object getTarget() {
		return target;
	}
}
