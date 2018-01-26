package com.example.springboot.common.mvc.filter;

import com.example.springboot.common.mvc.Holder;

public interface FilterChain {

	void doFilter(Holder request, Holder response) throws Throwable;
}
