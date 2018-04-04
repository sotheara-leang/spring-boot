package com.example.springboot.common.dispatcher.resolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.TypeUtils;

import com.example.springboot.common.dispatcher.annotation.Header;
import com.example.springboot.common.dispatcher.exception.ParameterRequiredException;
import com.example.springboot.common.dispatcher.model.Headers;
import com.example.springboot.common.dispatcher.model.Parameter;
import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.JavaType;

public class HeaderParameterResolver implements ParameterResolver {

	@Override
	public boolean supportsParameter( Parameter parameter ) {
		return parameter.hasAnnotation( Header.class );
	}

	@Override
	public Object resolveParameter( Parameter parameter, Request request ) {
		String paramName = parameter.getName();
		Headers headers = request.getHeaders();
		
		Header annotation = parameter.getAnnotation( Header.class );
		String targetParamName = StringUtils.isBlank( annotation.value() ) ? paramName : annotation.value();
		
		Object headerValue = headers.get( targetParamName );
		if ( headerValue == null ) {
			if ( annotation.required() ) {
				throw new ParameterRequiredException( targetParamName );
			}
		} else {
			Class<?> methodParamType = ClassUtils.primitiveToWrapper( parameter.getType() );
			if (!methodParamType.isAssignableFrom( headerValue.getClass() )) {
				Type[] paramGenericTypes = parameter.getGenericTypes();
				if (paramGenericTypes == null) {
					headerValue = ObjectMapperUtils.convert( headerValue, parameter.getType() );
				} else {
					ParameterizedType msgDataParameterizedType = TypeUtils.parameterize(parameter.getType(), paramGenericTypes);
					JavaType msgDataJavaType = ObjectMapperUtils.OBJECT_MAPPER.getTypeFactory().constructFromCanonical( msgDataParameterizedType.getTypeName() );
					
					headerValue = ObjectMapperUtils.convert( headerValue, msgDataJavaType );
				}
			}
		}

		return headerValue;
	}
}
