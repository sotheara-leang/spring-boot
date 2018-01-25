package com.example.springboot.common.mvc.filler;

import com.example.springboot.common.mvc.domain.Request;
import com.example.springboot.common.mvc.domain.Response;

public interface FilterChain {

	void doFilter( Request request, Response response ) throws Throwable;
}
