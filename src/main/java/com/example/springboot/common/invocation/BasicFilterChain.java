package com.example.springboot.common.invocation;

import java.util.Iterator;
import java.util.List;

public class BasicFilterChain implements FilterChain {

	private List<? extends Filter> filterList;

	private Iterator<? extends Filter> iterator;

	public BasicFilterChain(List<? extends Filter> filterList) {
		this.filterList = filterList;
	}

	@Override
	public void doFilter(Holder request, Holder response) throws Throwable {
		if (this.iterator == null) {
			this.iterator = this.filterList.iterator();
		}

		if (this.iterator.hasNext()) {
			Filter nextFilter = this.iterator.next();
			nextFilter.doFilter(request, response, this);
		}
	}

	public List<? extends Filter> getFilterList() {
		return filterList;
	}
}
