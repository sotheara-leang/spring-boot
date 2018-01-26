package com.example.springboot.common.mvc.filter;

import com.example.springboot.common.mvc.Holder;

public interface Filter {

	void doFilter(Holder request, Holder response, FilterChain filterChain) throws Throwable;
}
