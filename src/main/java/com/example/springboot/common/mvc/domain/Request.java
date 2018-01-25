package com.example.springboot.common.mvc.domain;

public class Request {

	private Object header;
	private Object body;
	
	public Object getHeader() {
		return header;
	}
	public void setHeader( Object header ) {
		this.header = header;
	}
	public Object getBody() {
		return body;
	}
	public void setBody( Object body ) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "Request [header=" + header + ", body=" + body + "]";
	}
}
