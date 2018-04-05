package com.example.springboot.dispatcher.exception;

import org.springframework.stereotype.Component;

import com.example.springboot.common.dispatcher.exception.ExceptionHandler;
import com.example.springboot.common.dispatcher.model.Request;

@Component
public class MyExceptionHandler implements ExceptionHandler<Exception> {

	@Override
	public Object resolveException( Request request, Exception exception ) {
		return "Error Sample";
	}
}
