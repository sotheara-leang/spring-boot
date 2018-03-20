package com.example.springboot.service.filter;

import com.example.springboot.common.dispatcher.filter.Filter;
import com.example.springboot.common.dispatcher.filter.FilterChain;
import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionServiceFilter implements Filter {

	@Override
	public Response doFilter(Request request, FilterChain filterChain) throws Throwable {
		log.debug("Before invoke TxServiceFilter");
		
		Response response = filterChain.doFilter(request);
		
		log.debug("After invoke TxServiceFilter");
		
		return response;
	}
}
