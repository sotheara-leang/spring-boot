package com.example.springboot.common.dispatcher.filter;

import java.util.List;

import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;

public interface FilterChain {

	Response doFilter(Request request) throws Throwable;
	
	List<Filter> getFilterList();
}
