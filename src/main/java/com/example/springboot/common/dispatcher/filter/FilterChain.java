package com.example.springboot.common.dispatcher.filter;

import java.util.List;

import com.example.springboot.common.dispatcher.model.Message;

public interface FilterChain {

	Message doFilter(Message request) throws Throwable;
	
	List<Filter> getFilterList();
}
