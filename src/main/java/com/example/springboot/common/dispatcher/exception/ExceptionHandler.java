package com.example.springboot.common.dispatcher.exception;

import com.example.springboot.common.dispatcher.model.Request;

public interface ExceptionHandler<T extends Exception> {

	Object resolveException( Request request, T exception );
}
