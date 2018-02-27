package com.example.springboot.common.mvc.filter;

import com.example.springboot.common.mvc.model.Message;

public interface Filter {

	Message doFilter(Message request, FilterChain filterChain) throws Throwable;
}
