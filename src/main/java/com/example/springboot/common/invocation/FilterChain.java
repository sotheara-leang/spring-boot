package com.example.springboot.common.invocation;

public interface FilterChain {

	void doFilter(Holder request, Holder response) throws Throwable;
}
