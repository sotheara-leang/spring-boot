package com.example.springboot.common.invocation;

public interface Filter {

	void doFilter(Holder request, Holder response, FilterChain filterChain) throws Throwable;
}
