package com.example.springboot.common.mvc.filter;

import java.util.List;

import com.example.springboot.common.mvc.model.Message;

public class BasicFilterChain extends AbstractFilterChain {

	public BasicFilterChain( List<Filter> filterList ) {
		super( filterList );
	}

	@Override
	public Message doFilter( Message request ) throws Throwable {
		SingleThreadFilterChain internalFilterChain = new SingleThreadFilterChain( filterList );
		return internalFilterChain.doFilter( request );
	}
}
