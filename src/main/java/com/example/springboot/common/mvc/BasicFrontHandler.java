package com.example.springboot.common.mvc;

import java.util.List;

import com.example.springboot.common.mvc.dispatcher.Dispatcher;
import com.example.springboot.common.mvc.filter.Filter;
import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.common.mvc.model.Message;

public class BasicFrontHandler implements FrontHandler {

	protected FilterChain filterChain;
	
	protected Dispatcher dispatcher;

	public BasicFrontHandler( FilterChain filterChain, Dispatcher dispatcher ) {
		this.filterChain = filterChain;
		this.dispatcher = dispatcher;
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public Message execute( Message request ) throws Throwable {
		Message response = new Message();
		
		if (filterChain != null) {
			List<Filter> filterList = (List<Filter>) filterChain.getFilterList();
			
			Filter lastFilter = null;
			if (filterList != null && filterList.size() > 0) {
				lastFilter = filterList.get( filterList.size() - 1 );
			}
			
			if (lastFilter == null || !LastFilter.class.isAssignableFrom( lastFilter.getClass() )) {
				lastFilter = new LastFilter();
				filterList.add( lastFilter );
			}
			
			filterChain.doFilter( request, response );
		} else {
			dispatcher.dispatch( request, response );
		}
		
		return response;
	}
	
	class LastFilter implements Filter {
		
		@Override
		public void doFilter( Message request, Message response, FilterChain filterChain ) throws Throwable {
			dispatcher.dispatch( request, response );
		}
	}
}
