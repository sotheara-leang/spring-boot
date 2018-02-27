package com.example.springboot.common.dispatcher.model;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class MethodParameter {

	private String parameterName;
	private Class<?> parameterType;
	private int parameterIndex;
	private Annotation[] parameterAnnotations;
	
	public String getParameterName() {
		return parameterName;
	}
	
	public void setParameterName( String parameterName ) {
		this.parameterName = parameterName;
	}
	
	public Class<?> getParameterType() {
		return parameterType;
	}
	
	public void setParameterType( Class<?> parameterType ) {
		this.parameterType = parameterType;
	}
	
	public int getParameterIndex() {
		return parameterIndex;
	}
	
	public void setParameterIndex( int parameterIndex ) {
		this.parameterIndex = parameterIndex;
	}
	
	public Annotation[] getParameterAnnotations() {
		return parameterAnnotations;
	}
	
	public void setParameterAnnotations( Annotation[] parameterAnnotations ) {
		this.parameterAnnotations = parameterAnnotations;
	}
	
	@SuppressWarnings("unchecked")
	public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
		Annotation[] anns = getParameterAnnotations();
		for (Annotation ann : anns) {
			if (annotationType.isInstance(ann)) {
				return (A) ann;
			}
		}
		return null;
	}
	
	public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType) {
		return (getParameterAnnotation(annotationType) != null);
	}

	@Override
	public String toString() {
		return "MethodParameter [parameterName=" + parameterName + ", parameterType=" + parameterType
				+ ", parameterIndex=" + parameterIndex + ", parameterAnnotations="
				+ Arrays.toString( parameterAnnotations ) + "]";
	}
}
