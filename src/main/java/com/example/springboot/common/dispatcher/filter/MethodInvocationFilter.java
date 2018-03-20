package com.example.springboot.common.dispatcher.filter;

import com.example.springboot.common.dispatcher.model.MethodInvocation;
import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;

public class MethodInvocationFilter implements Filter {

	@Override
	public Response doFilter( Request request, FilterChain filterChain ) throws Throwable {
		Response response = new Response();
		
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
