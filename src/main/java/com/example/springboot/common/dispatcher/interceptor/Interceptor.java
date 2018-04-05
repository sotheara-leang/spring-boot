package com.example.springboot.common.dispatcher.interceptor;

import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;

public interface Interceptor {

	boolean preProcess( Request request, Response response );
	
	void postProcess( Request request, Response response );
}
