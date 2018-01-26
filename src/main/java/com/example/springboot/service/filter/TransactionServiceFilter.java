package com.example.springboot.service.filter;

import org.springframework.stereotype.Component;

import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.common.mvc.model.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TransactionServiceFilter extends ServiceFilter {

	@Override
	public void doFilter(Message<Object> request, Message<Object> response, FilterChain filterChain) throws Throwable {
		log.debug("Before invoke TxServiceFilter");
		
		filterChain.doFilter(request, response);
		
		log.debug("After invoke TxServiceFilter");
	}
}
