package com.example.springboot.service.filter;

import com.example.springboot.common.mvc.filter.Filter;
import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.common.mvc.model.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingServiceFilter implements Filter {

	@Override
	public Message doFilter(Message request, FilterChain filterChain) throws Throwable {
		log.debug("Before invoke LogServiceFilter");
		
		Message response = filterChain.doFilter(request);
		
		log.debug("After invoke LogServiceFilter");
		
		return response;
	}
}
