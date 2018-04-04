package com.example.springboot.common.dispatcher.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;

public class Parameter {

	private String name;
	private Class<?> type;
	private Type[] genericTypes;
	private int index;
	private Annotation[] annotations;
	
	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public Class<?> getType() {
		return type;
	}

	public void setType( Class<?> type ) {
		this.type = type;
	}

	public Type[] getGenericTypes() {
		return genericTypes;
	}

	public void setGenericTypes( Type[] genericTypes ) {
		this.genericTypes = genericTypes;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex( int index ) {
		this.index = index;
	}

	public Annotation[] getAnnotations() {
		return annotations;
	}

	public void setAnnotations( Annotation[] annotations ) {
		this.annotations = annotations;
	}

	@SuppressWarnings("unchecked")
	public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
		Annotation[] anns = getAnnotations();
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
		return "MethodParameter [name=" + name + ", type=" + type + ", genericTypes=" + Arrays.toString( genericTypes )
				+ ", index=" + index + ", annotations=" + Arrays.toString( annotations ) + "]";
	}
}
