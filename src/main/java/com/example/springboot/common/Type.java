package com.example.springboot.common;

import com.fasterxml.jackson.annotation.JsonValue;

public interface Type<T> {

	@JsonValue
	T getValue();
	
	String getDescription();
}
