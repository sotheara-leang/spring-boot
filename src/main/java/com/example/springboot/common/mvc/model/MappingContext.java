package com.example.springboot.common.mvc.model;

public class MappingContext {

	private String path;
	private Class<?> accept;
	private MethodInvocation methodInvocation;
	
	public String getPath() {
		return path;
	}
	
	public void setPath( String path ) {
		this.path = path;
	}
	
	public Class<?> getAccept() {
		return accept;
	}
	
	public void setAccept( Class<?> accept ) {
		this.accept = accept;
	}
	
	public MethodInvocation getMethodInvocation() {
		return methodInvocation;
	}

	public void setMethodInvocation( MethodInvocation methodInvocation ) {
		this.methodInvocation = methodInvocation;
	}

	@Override
	public String toString() {
		return "MappingContext [path=" + path + ", accept=" + accept + ", methodInvocation=" + methodInvocation + "]";
	}
}
