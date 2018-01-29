package com.example.springboot.common.mvc.filter;

import com.example.springboot.common.mvc.model.Message;
import com.example.springboot.common.mvc.model.MethodInvocation;

public class MethodInvocationFilter implements Filter {

	@Override
	public void doFilter( Message request, Message response, FilterChain filterChain ) throws Throwable {
		if (request == null || (request.getBody() != null 
				&& MethodInvocation.class.isAssignableFrom( request.getBody().getClass() ))) {
			
			MethodInvocation methodInvocation = (MethodInvocation) request.getBody();
			response.setBody( methodInvocation.proceed() );
		} else {
			filterChain.doFilter( request, response );
		}
	}
}
