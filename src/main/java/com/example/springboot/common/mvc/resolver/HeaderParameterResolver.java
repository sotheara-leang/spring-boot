package com.example.springboot.common.mvc.resolver;

import org.apache.commons.lang.ClassUtils;
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
		String parameterName = StringUtils.isBlank( annotation.value() ) ? parameter.getParameterName() : annotation.value();
		
		MessageHeaders headers = request.getHeaders();
		Object headerValue = headers.get( parameterName );
		if (headerValue == null) {
			if (annotation.required()) {
				throw new ParameterRequiredException( parameterName );
			}
		} else {
			Class<?> methodParamType = ClassUtils.primitiveToWrapper( parameter.getParameterType() );
			if (!methodParamType.isAssignableFrom( headerValue.getClass() )) {
				throw new ParameterTypeInvalidException( parameterName );
			}
		}
		
		return headerValue;
	}
}
