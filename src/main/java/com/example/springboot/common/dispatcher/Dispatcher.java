package com.example.springboot.common.dispatcher;

import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;

public interface Dispatcher {

	Response process(Request request) throws Throwable;
}
