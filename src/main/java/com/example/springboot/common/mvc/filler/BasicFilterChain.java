package com.example.springboot.common.mvc.filler;

import java.util.Iterator;
import java.util.List;

import org.springframework.util.Assert;

import com.example.springboot.common.mvc.domain.Request;
import com.example.springboot.common.mvc.domain.Response;
import com.example.springboot.common.mvc.handler.Handler;

public class BasicFilterChain implements FilterChain {

	private List<? extends Filter> filterList;

	private Iterator<? extends Filter> iterator;

	private Handler handler;

	public BasicFilterChain( List<? extends Filter> filterList ) {
		this.filterList = filterList;
	}
	
	@Override
	public void doFilter( Request request, Response response ) throws Throwable {
		Assert.notNull( request, "request must not be null" );
		Assert.notNull( response, "response must not be null" );
		
		if ( this.iterator == null ) {
			this.iterator = this.filterList.iterator();
		}

		if ( this.iterator.hasNext() ) {
			Filter nextFilter = this.iterator.next();
			nextFilter.doFilter( request, response, this );
		} else {
			handler.execute(request, response);
		}
	}

	public Handler getHandler() {
		return handler;
	}
	
	public void setHandler( Handler handler ) {
		this.handler = handler;
	}

	public List<? extends Filter> getFilterList() {
		return filterList;
	}
}
