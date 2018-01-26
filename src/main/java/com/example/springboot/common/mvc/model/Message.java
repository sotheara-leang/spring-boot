package com.example.springboot.common.mvc.model;

public class Message<T> {

	private MessageHeaders headers;
	
	private T body;
	
	public Message() {}
	
	public Message(T body) {
		this(null, body);
	}
	
	public Message(MessageHeaders headers, T body) {
		this.headers = headers;
		this.body = body;
	}

	public MessageHeaders getHeaders() {
		return headers;
	}

	public void setHeaders( MessageHeaders headers ) {
		this.headers = headers;
	}

	public T getBody() {
		return body;
	}

	public void setBody( T body ) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Message [headers=" + headers + ", body=" + body + "]";
	}
}
