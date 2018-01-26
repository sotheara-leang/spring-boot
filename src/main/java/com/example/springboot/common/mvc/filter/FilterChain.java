package com.example.springboot.common.mvc.filter;

import com.example.springboot.common.mvc.model.Message;

public interface FilterChain {

	void doFilter(Message<Object> request, Message<Object> response) throws Throwable;
}
