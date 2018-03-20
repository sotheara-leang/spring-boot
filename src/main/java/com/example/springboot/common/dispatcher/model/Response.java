package com.example.springboot.common.dispatcher.model;

public class Response {

	private Headers headers = new Headers();
	private Object 	body;
	
	public Headers getHeaders() {
		return headers;
	}
	
	public void setHeaders( Headers headers ) {
		this.headers = headers;
	}
	
	public Object getBody() {
		return body;
	}
	
	public void setBody( Object body ) {
		this.body = body;
	}
	
	public Object getHeader(Object name) {
		return headers.get( name );
	}
	
	public <T> T get(Object name, Class<T> type) {
		return headers.get( name, type );
	}
	
	public boolean containsHeader(Object name) {
		return this.headers.containsKey(name);
	}
	
	public void setHeader(Object name, Object value) {
		headers.put( name, value );
	}

	@Override
	public String toString() {
		return "Response [headers=" + headers + ", body=" + body + "]";
	}
}
