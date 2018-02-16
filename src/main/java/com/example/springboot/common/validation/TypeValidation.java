package com.example.springboot.common.validation;

public interface TypeValidation<T> {

	FieldValidation<T> field(String fieldName);
	
	CompositeFieldValidation<T> compositeField(String fieldName);
}
