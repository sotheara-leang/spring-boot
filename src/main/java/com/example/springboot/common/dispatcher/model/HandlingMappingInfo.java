package com.example.springboot.common.dispatcher.model;

import java.lang.reflect.Method;

public class HandlingMappingInfo {

	private String path;
	private Method method;
	private Object handler;
	
	public String getPath() {
		return path;
	}
	
	public void setPath( String path ) {
		this.path = path;
	}
	
	public Method getMethod() {
		return method;
	}
	
	public void setMethod( Method method ) {
		this.method = method;
	}
	
	public Object getHandler() {
		return handler;
	}
	
	public void setHandler( Object handler ) {
		this.handler = handler;
	}
	
	@Override
	public String toString() {
		return "HandlingMappingInfo [path=" + path + ", method=" + method + ", handler=" + handler + "]";
	}
}
