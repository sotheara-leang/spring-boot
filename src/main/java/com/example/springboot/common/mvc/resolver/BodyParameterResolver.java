package com.example.springboot.common.mvc.resolver;

import com.example.springboot.common.mvc.annotation.Body;
import com.example.springboot.common.mvc.exception.ParameterRequiredException;
import com.example.springboot.common.mvc.model.Message;
import com.example.springboot.common.mvc.model.MethodParameter;

public class BodyParameterResolver implements HandlerMethodParameterResolver {

	@Override
	public boolean supportsParameter( MethodParameter parameter ) {
		return parameter.hasParameterAnnotation( Body.class );
	}
	
	@Override
	public Object resolveParemeter( MethodParameter parameter, Message request ) {
		Body annotation = parameter.getParameterAnnotation( Body.class );
		Object body = request.getBody();
		if (body == null && annotation.required()) {
			throw new ParameterRequiredException();
		}
		return body;
	}
}
