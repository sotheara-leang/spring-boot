package com.example.springboot.common.dispatcher.filter;

import com.example.springboot.common.dispatcher.model.Message;
import com.example.springboot.common.dispatcher.model.MethodInvocation;

public class MethodInvocationFilter implements Filter {

	@Override
	public Message doFilter( Message request, FilterChain filterChain ) throws Throwable {
		Message response = new Message();
		
		if (request == null || (request.getBody() != null 
				&& MethodInvocation.class.isAssignableFrom( request.getBody().getClass() ))) {
			
			MethodInvocation methodInvocation = (MethodInvocation) request.getBody();
			response.setBody( methodInvocation.proceed() );
		} else {
			response = filterChain.doFilter( request );
		}
		
		return response;
	}
}
