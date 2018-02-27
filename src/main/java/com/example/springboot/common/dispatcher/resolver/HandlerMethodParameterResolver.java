package com.example.springboot.common.mvc.resolver;

import com.example.springboot.common.mvc.model.Message;
import com.example.springboot.common.mvc.model.MethodParameter;

public interface HandlerMethodParameterResolver {

	boolean supportsParameter(MethodParameter parameter);
	
	Object resolveParemeter(MethodParameter parameter, Message request);
}
