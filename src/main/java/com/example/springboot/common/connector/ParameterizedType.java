package com.example.springboot.common.connector;

import java.lang.reflect.Type;

public class ParameterizedType implements java.lang.reflect.ParameterizedType {
	
	private java.lang.reflect.ParameterizedType delegate;
    private Type[] actualTypeArguments;

    public ParameterizedType(java.lang.reflect.ParameterizedType delegate, Type[] actualTypeArguments) {
        this.delegate = delegate;
        this.actualTypeArguments = actualTypeArguments;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return actualTypeArguments;
    }

    @Override
    public Type getRawType() {
        return delegate.getRawType();
    }

    @Override
    public Type getOwnerType() {
        return delegate.getOwnerType();
    }
}