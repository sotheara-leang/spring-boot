package com.example.springboot.common.dispatcher.resolver;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.reflect.TypeUtils;

import com.example.springboot.common.dispatcher.annotation.Body;
import com.example.springboot.common.dispatcher.exception.ParameterRequiredException;
import com.example.springboot.common.dispatcher.model.Parameter;
import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.util.ObjectMapperUtils;
import com.fasterxml.jackson.databind.JavaType;

public class BodyParameterResolver implements ParameterResolver {

	@Override
	public boolean supportsParameter( Parameter parameter ) {
		return parameter.hasAnnotation( Body.class );
	}
	
	@Override
	public Object resolveParameter( Parameter parameter, Request request ) {
		Object convertedValue = null;
		
		Body annotation = parameter.getAnnotation( Body.class );
		Object requestBody = request.getBody();
		
		if ( requestBody == null ) {
			if ( annotation.required() ) {
				throw new ParameterRequiredException();
			}
		} else {
			Type[] paramGenericTypes = parameter.getGenericTypes();
			if (paramGenericTypes == null) {
				convertedValue = ObjectMapperUtils.convert( requestBody, parameter.getType() );
			} else {
				ParameterizedType msgDataParameterizedType = TypeUtils.parameterize(parameter.getType(), paramGenericTypes);
				JavaType msgDataJavaType = ObjectMapperUtils.OBJECT_MAPPER.getTypeFactory().constructFromCanonical( msgDataParameterizedType.getTypeName() );
				
				convertedValue = ObjectMapperUtils.convert( requestBody, msgDataJavaType );
			}
		}
		
		return convertedValue;
	}
}
