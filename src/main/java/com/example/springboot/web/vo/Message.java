package com.example.springboot.web.vo;

public class Message<T> {

	private String header;
	private T body;
	
	public String getHeader() {
		return header;
	}
	public void setHeader( String header ) {
		this.header = header;
	}
	public T getBody() {
		return body;
	}
	public void setBody( T body ) {
		this.body = body;
	}
	@Override
	public String toString() {
		return "Message [header=" + header + ", body=" + body + "]";
	}
}
