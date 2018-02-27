package com.example.springboot.common.dispatcher.resolver;

import com.example.springboot.common.dispatcher.model.Message;
import com.example.springboot.common.dispatcher.model.MethodParameter;

public interface HandlerMethodParameterResolver {

	boolean supportsParameter(MethodParameter parameter);
	
	Object resolveParemeter(MethodParameter parameter, Message request);
}
