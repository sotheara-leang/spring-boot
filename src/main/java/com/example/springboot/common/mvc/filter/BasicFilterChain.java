package com.example.springboot.common.mvc.filter;

import java.util.Iterator;
import java.util.List;

import com.example.springboot.common.mvc.model.Message;

public class BasicFilterChain implements FilterChain {

	private List<Filter> filterList;

	private Iterator<Filter> iterator;

	public BasicFilterChain(List<Filter> filterList) {
		this.filterList = filterList;
	}

	@Override
	public void doFilter(Message request, Message response) throws Throwable {
		if (this.iterator == null) {
			this.iterator = this.filterList.iterator();
		}

		if (this.iterator.hasNext()) {
			Filter nextFilter = this.iterator.next();
			nextFilter.doFilter(request, response, this);
		}
	}

	public List<Filter> getFilterList() {
		return filterList;
	}
}
