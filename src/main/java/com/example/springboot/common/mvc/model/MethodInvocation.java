package com.example.springboot.common.mvc.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodInvocation {

	private Method method;
	private Object[] params;
	private Object target;
	
	public MethodInvocation( Object target, Method method, Object[] params ) {
		this.method = method;
		this.params = params;
		this.target = target;
	}

	public Object proceed() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return method.invoke( target, params );
	}

	public Method getMethod() {
		return method;
	}
	
	public void setMethod( Method method ) {
		this.method = method;
	}
	
	public Object[] getParams() {
		return params;
	}
	
	public void setParams( Object[] params ) {
		this.params = params;
	}
	
	public Object getTarget() {
		return target;
	}

	public void setTarget( Object target ) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "MethodInvocation [method=" + method + ", params=" + Arrays.toString( params ) + ", target=" + target
				+ "]";
	}
}
