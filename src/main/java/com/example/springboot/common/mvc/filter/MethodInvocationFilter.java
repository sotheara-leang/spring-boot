package com.example.springboot.common.mvc.filter;

import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import com.example.springboot.common.mvc.model.Message;

public class MethodInvocationFilter implements Filter {

	@Override
	public void doFilter( Message<Object> request, Message<Object> response, FilterChain filterChain ) throws Throwable {
		if (request == null || (request.getBody() != null 
				&& MethodInvocationProceedingJoinPoint.class.isAssignableFrom( request.getBody().getClass() ))) {
			MethodInvocationProceedingJoinPoint jp = (MethodInvocationProceedingJoinPoint) request.getBody();
			response.setBody( jp.proceed() );
		} else {
			filterChain.doFilter( request, response );
		}
	}
}
