package com.example.springboot.filterchain;

import com.example.springboot.common.invocation.FilterChain;
import com.example.springboot.common.invocation.Holder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogServiceFilter extends ServiceFilter {

	@Override
	public void doFilter(Holder request, Holder response, FilterChain filterChain) throws Throwable {
		log.debug("Before invoke service - MLogServiceFilter");
		
		filterChain.doFilter(request, response);
		
		log.debug("After invoke service - MLogServiceFilter");
	}
}
