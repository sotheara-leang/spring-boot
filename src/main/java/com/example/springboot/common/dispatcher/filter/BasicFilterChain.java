package com.example.springboot.common.dispatcher.filter;

import java.util.List;

import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;

public class BasicFilterChain extends AbstractFilterChain {

	public BasicFilterChain( List<Filter> filterList ) {
		super( filterList );
	}

	@Override
	public Response doFilter( Request request ) throws Throwable {
		SingleThreadFilterChain internalFilterChain = new SingleThreadFilterChain( filterList );
		return internalFilterChain.doFilter( request );
	}
}
