package com.example.springboot.common.mvc.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageHeaders {

	public static final String REQ_PATH = "requestPath";
	
	private Map<String, Object> headers = new HashMap<>();
	
	public String getRequestUrl() {
		return (String) get( REQ_PATH );
	}
	
	public boolean containsKey(Object key) {
		return this.headers.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return this.headers.containsValue(value);
	}

	public Set<Map.Entry<String, Object>> entrySet() {
		return Collections.unmodifiableMap(this.headers).entrySet();
	}

	public Object get(Object key) {
		return this.headers.get(key);
	}

	public boolean isEmpty() {
		return this.headers.isEmpty();
	}

	public Set<String> keySet() {
		return Collections.unmodifiableSet(this.headers.keySet());
	}

	public int size() {
		return this.headers.size();
	}
	
	public Collection<Object> values() {
		return Collections.unmodifiableCollection(this.headers.values());
	}
	
	public Object put(String key, Object value) {
		throw new UnsupportedOperationException("MessageHeaders is immutable");
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(Object key, Class<T> type) {
		Object value = this.headers.get(key);
		if (value == null) {
			return null;
		}
		if (!type.isAssignableFrom(value.getClass())) {
			throw new IllegalArgumentException("Incorrect type specified for header '" +
					key + "'. Expected [" + type + "] but actual type is [" + value.getClass() + "]");
		}
		return (T) value;
	}
	
	protected Map<String, Object> getRawHeaders() {
		return this.headers;
	}
	
	public void putAll(Map<? extends String, ? extends Object> map) {
		throw new UnsupportedOperationException("MessageHeaders is immutable");
	}
	
	@Override
	public String toString() {
		return "MessageHeaders [headers=" + headers + "]";
	}
}
