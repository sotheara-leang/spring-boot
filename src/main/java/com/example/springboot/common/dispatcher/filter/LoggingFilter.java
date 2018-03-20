package com.example.springboot.common.dispatcher.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;

public class LoggingFilter implements Filter {

	private static Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
	
	@Override
	public Response doFilter(Request request, FilterChain filterChain) throws Throwable {
		logger.debug("Accept request message: {}", request);
		
		Response response = null;
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
