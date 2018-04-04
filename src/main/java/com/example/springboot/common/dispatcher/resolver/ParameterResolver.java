package com.example.springboot.common.dispatcher.resolver;

import com.example.springboot.common.dispatcher.model.Parameter;
import com.example.springboot.common.dispatcher.model.Request;

public interface ParameterResolver {

	boolean supportsParameter(Parameter parameter);
	
	Object resolveParameter(Parameter parameter, Request request);
}
