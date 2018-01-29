package com.example.springboot.common.mvc.resolver;

import org.apache.commons.lang.StringUtils;

import com.example.springboot.common.mvc.annotation.Header;
import com.example.springboot.common.mvc.exception.ParameterRequiredException;
import com.example.springboot.common.mvc.exception.ParameterTypeInvalidException;
import com.example.springboot.common.mvc.model.Message;
import com.example.springboot.common.mvc.model.MessageHeaders;
import com.example.springboot.common.mvc.model.MethodParameter;

public class HeaderParameterResolver implements HandlerMethodParameterResolver {

	@Override
	public boolean supportsParameter( MethodParameter parameter ) {
		return parameter.hasParameterAnnotation( Header.class );
	}
	
	@Override
	public Object resolveParemeter( MethodParameter parameter, Message request ) {
		Header annotation = parameter.getParameterAnnotation( Header.class );
		String parameterName = StringUtils.isBlank( annotation.name() ) ? parameter.getParameterName() : annotation.name();
		
		MessageHeaders headers = request.getHeaders();
		Object header = headers.get( parameterName );
		if (header == null) {
			if (annotation.required()) {
				throw new ParameterRequiredException( parameterName );
			}
		} else if (!parameter.getParameterType().isAssignableFrom( header.getClass() )) {
			throw new ParameterTypeInvalidException( parameterName );
		}
		
		return header;
	}
}
