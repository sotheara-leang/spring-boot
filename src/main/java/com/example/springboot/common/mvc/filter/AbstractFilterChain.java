package com.example.springboot.common.mvc.filter;

import java.util.List;

public abstract class AbstractFilterChain implements FilterChain {

	protected List<Filter> filterList;
	
	public AbstractFilterChain(List<Filter> filterList) {
		this.filterList = filterList;
	}

	@Override
	public List<Filter> getFilterList() {
		return filterList;
	}

	public void setFilterList( List<Filter> filterList ) {
		this.filterList = filterList;
	}
}
