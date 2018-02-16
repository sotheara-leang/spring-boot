package com.example.springboot.common.validation;

import com.example.springboot.common.validation.constraint.Constraint;

public interface FieldValidation<T> {

	<C extends Constraint<C>> Constraint<C> constraint(Class<C> clazz);
}
