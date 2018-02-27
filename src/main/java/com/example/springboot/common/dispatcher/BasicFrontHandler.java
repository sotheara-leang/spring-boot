package com.example.springboot.common.dispatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.springboot.common.dispatcher.filter.Filter;
import com.example.springboot.common.dispatcher.filter.FilterChain;
import com.example.springboot.common.dispatcher.filter.SingleThreadFilterChain;
import com.example.springboot.common.dispatcher.model.Message;

public class BasicFrontHandler implements FrontHandler {

	protected List<Filter> filterList = Collections.emptyList();
	
	protected Dispatcher dispatcher;

	public BasicFrontHandler( List<Filter> filterList, Dispatcher dispatcher ) {
		this.filterList = filterList;
		this.dispatcher = dispatcher;
	}

	@Override
	public Message execute( Message request ) throws Throwable {
		List<Filter> internalFilterList = new ArrayList<Filter>(filterList);
		
		Filter lastFilter = null;
		if (internalFilterList.size() > 0) {
			lastFilter = internalFilterList.get( internalFilterList.size() - 1 );
		}
		
		if (lastFilter == null || !LastFilter.class.isAssignableFrom( lastFilter.getClass() )) {
			lastFilter = new LastFilter();
			internalFilterList.add( lastFilter );
		}
		
		FilterChain internalFilterChain = new SingleThreadFilterChain( internalFilterList );
		return internalFilterChain.doFilter( request );
	}
	
	class LastFilter implements Filter {
		
		@Override
		public Message doFilter( Message request, FilterChain filterChain ) throws Throwable {
			return dispatcher.dispatch( request );
		}
	}
}
