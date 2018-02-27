package com.example.springboot.common.mvc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.mvc.model.Message;

public class LoggingFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
	
	@Override
	public Message doFilter(Message request, FilterChain filterChain) throws Throwable {
		logger.debug("Accept request message: {}", request);
		
		Message response = null;
		try {
			response = filterChain.doFilter(request);
		} catch (Exception e) {
			logger.error( "Error in downstream filters", e);
			throw e;
		}
		
		logger.debug("Return response message: {}", response);
		
		return response;
	}
}
