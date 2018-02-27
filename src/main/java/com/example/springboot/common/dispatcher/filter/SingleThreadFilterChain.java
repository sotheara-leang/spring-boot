package com.example.springboot.common.dispatcher.filter;

import java.util.Iterator;
import java.util.List;

import com.example.springboot.common.dispatcher.model.Message;

public class SingleThreadFilterChain extends AbstractFilterChain {

	private Iterator<Filter> iterator;

	public SingleThreadFilterChain(List<Filter> filterList) {
		super(filterList);
		
		iterator = filterList.iterator();
	}

	@Override
	public Message doFilter(Message request) throws Throwable {
		Message response = null;
		
		if (iterator == null) {
			iterator = filterList.iterator();
		}
		
		if (this.iterator.hasNext()) {
			Filter nextFilter = this.iterator.next();
			response = nextFilter.doFilter(request, this);
		}
		
		return response;
	}
}
