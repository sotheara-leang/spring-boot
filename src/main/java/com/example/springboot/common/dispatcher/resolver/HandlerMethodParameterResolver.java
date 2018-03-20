package com.example.springboot.common.dispatcher.resolver;

import com.example.springboot.common.dispatcher.model.MethodParameter;
import com.example.springboot.common.dispatcher.model.Request;

public interface HandlerMethodParameterResolver {

	boolean supportsParameter(MethodParameter parameter);
	
	Object resolveParemeter(MethodParameter parameter, Request request);
}
