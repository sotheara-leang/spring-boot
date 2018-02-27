package com.example.springboot.common.mvc;

import java.util.List;

import com.example.springboot.common.mvc.dispatcher.Dispatcher;
import com.example.springboot.common.mvc.filter.Filter;
import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.common.mvc.filter.SingleThreadFilterChain;
import com.example.springboot.common.mvc.model.Message;

public class BasicFrontHandler implements FrontHandler {

	protected List<Filter> filterList;
	
	protected Dispatcher dispatcher;

	public BasicFrontHandler( List<Filter> filterList, Dispatcher dispatcher ) {
		this.filterList = filterList;
		this.dispatcher = dispatcher;
	}

	@Override
	public Message execute( Message request ) throws Throwable {
		Filter lastFilter = null;
		if (filterList != null && filterList.size() > 0) {
			lastFilter = filterList.get( filterList.size() - 1 );
		}
		
		if (lastFilter == null || !LastFilter.class.isAssignableFrom( lastFilter.getClass() )) {
			lastFilter = new LastFilter();
			filterList.add( lastFilter );
		}
		
		FilterChain internalFilterChain = new SingleThreadFilterChain( filterList );
		return internalFilterChain.doFilter( request );
	}
	
	class LastFilter implements Filter {
		
		@Override
		public Message doFilter( Message request, FilterChain filterChain ) throws Throwable {
			return dispatcher.dispatch( request );
		}
	}
}
