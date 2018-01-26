package com.example.springboot.common.mvc;

public class Holder {

	private Object data;

	public Holder() {

	}
	
	public Holder(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Holder [data=" + data + "]";
	}
}
