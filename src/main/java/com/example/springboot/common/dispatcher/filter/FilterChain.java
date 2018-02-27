package com.example.springboot.common.mvc.filter;

import java.util.List;

import com.example.springboot.common.mvc.model.Message;

public interface FilterChain {

	Message doFilter(Message request) throws Throwable;
	
	List<Filter> getFilterList();
}
