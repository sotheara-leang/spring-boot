package com.example.springboot.common.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class ObjectMapperUtils {
	
	public static final ObjectMapper OBJECT_MAPPER = defaultObjectMapper();

	public static ObjectMapper defaultObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper()
				.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true))
				.setSerializationInclusion(Include.NON_NULL)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		
		return objectMapper;
	}
	
	public <T> T convert(Object fromValue, TypeReference<T> toTypeReference ) {
		return OBJECT_MAPPER.convertValue( fromValue, toTypeReference );
	}
	
	public <T> T convert(Object fromValue, JavaType toJavaType ) {
		return OBJECT_MAPPER.convertValue( fromValue, toJavaType );
	}
}
