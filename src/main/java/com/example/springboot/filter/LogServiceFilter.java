package com.example.springboot.filter;

import org.springframework.stereotype.Service;

import com.example.springboot.common.mvc.domain.Request;
import com.example.springboot.common.mvc.domain.Response;
import com.example.springboot.common.mvc.filler.FilterChain;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LogServiceFilter extends ServiceFilter {
	
	@Override
	public void doFilter( Request request, Response response, FilterChain filterChain ) throws Throwable {
		log.debug( "Before invoke service" );
		filterChain.doFilter( request, response );
		log.debug( "After invoke service" );
	}
}
