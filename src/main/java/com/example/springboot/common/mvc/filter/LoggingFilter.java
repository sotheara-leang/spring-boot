package com.example.springboot.common.mvc.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.mvc.model.Message;

public class LoggingFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
	
	@Override
	public void doFilter(Message request, Message response, FilterChain filterChain) throws Throwable {
		logger.debug("Accept request message: {}", request);
		
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			logger.error( "Error in downstream chain", e);
			throw e;
		}
		
		logger.debug("Return response message: {}", response);
	}
}
