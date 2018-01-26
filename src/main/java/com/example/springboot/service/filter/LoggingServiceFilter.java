package com.example.springboot.service.filter;

import com.example.springboot.common.mvc.filter.Filter;
import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.common.mvc.model.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingServiceFilter implements Filter {

	@Override
	public void doFilter(Message request, Message response, FilterChain filterChain) throws Throwable {
		log.debug("Before invoke LogServiceFilter");
		
		filterChain.doFilter(request, response);
		
		log.debug("After invoke LogServiceFilter");
	}
}
