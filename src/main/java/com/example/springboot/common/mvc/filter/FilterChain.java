package com.example.springboot.common.mvc.filter;

import java.util.List;

import com.example.springboot.common.mvc.model.Message;

public interface FilterChain {

	void doFilter(Message request, Message response) throws Throwable;
	
	List<Filter> getFilterList();
}
