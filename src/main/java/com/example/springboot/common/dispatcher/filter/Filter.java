package com.example.springboot.common.dispatcher.filter;

import com.example.springboot.common.dispatcher.model.Message;

public interface Filter {

	Message doFilter(Message request, FilterChain filterChain) throws Throwable;
}
