package com.example.springboot.common.dispatcher.model;

import java.util.HashMap;

public class Headers extends HashMap<Object, Object> {

	private static final long serialVersionUID = 6521640200238191636L;
	
	public static final String REQ_PATH = "REQ_PATH";
	
	public static final String STATUS_CODE = "STATUS";
	
	@SuppressWarnings( "unchecked" )
	public <T> T get(Object key, Class<T> type) {
		Object value = get(key);
		if (value == null) {
			return null;
		}
		if (!type.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException("Incorrect type specified for header '" +
					key + "'. Expected [" + type + "] but actual type is [" + value.getClass() + "]");
		}
		return (T) value;
	}
}
