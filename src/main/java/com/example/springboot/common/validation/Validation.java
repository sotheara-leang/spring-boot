package com.example.springboot.common.validation;

public interface Validation {

	<T> TypeValidation<T> type(Class<T> type);
}
