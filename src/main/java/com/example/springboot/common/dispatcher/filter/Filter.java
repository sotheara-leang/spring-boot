package com.example.springboot.common.dispatcher.filter;

import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;

public interface Filter {

	Response doFilter(Request request, FilterChain filterChain) throws Throwable;
}
