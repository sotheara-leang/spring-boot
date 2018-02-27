package com.example.springboot.common.dispatcher.model;

public class Message {

	private MessageHeaders headers;
	
	private Object body;
	
	public Message() {}
	
	public Message(Object body) {
		this(null, body);
	}
	
	public Message(MessageHeaders headers, Object body) {
		this.headers = headers;
		this.body = body;
	}

	public MessageHeaders getHeaders() {
		return headers;
	}

	public void setHeaders( MessageHeaders headers ) {
		this.headers = headers;
	}

	public Object getBody() {
		return body;
	}

	public void setBody( Object body ) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "Message [headers=" + headers + ", body=" + body + "]";
	}
}
