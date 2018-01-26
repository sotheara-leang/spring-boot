package com.example.springboot.common.mvc.filter;

import com.example.springboot.common.mvc.model.Message;

public interface Filter {

	void doFilter(Message<Object> request, Message<Object> response, FilterChain filterChain) throws Throwable;
}
