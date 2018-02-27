package com.example.springboot.service.filter;

import com.example.springboot.common.dispatcher.filter.Filter;
import com.example.springboot.common.dispatcher.filter.FilterChain;
import com.example.springboot.common.dispatcher.model.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionServiceFilter implements Filter {

	@Override
	public Message doFilter(Message request, FilterChain filterChain) throws Throwable {
		log.debug("Before invoke TxServiceFilter");
		
		Message response = filterChain.doFilter(request);
		
		log.debug("After invoke TxServiceFilter");
		
		return response;
	}
}
