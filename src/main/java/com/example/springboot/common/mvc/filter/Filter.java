package com.example.springboot.common.mvc.filter;

import com.example.springboot.common.mvc.model.Message;

public interface Filter {

	void doFilter(Message request, Message response, FilterChain filterChain) throws Throwable;
}
