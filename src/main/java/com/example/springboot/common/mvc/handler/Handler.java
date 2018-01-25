package com.example.springboot.common.mvc.handler;

import com.example.springboot.common.mvc.domain.Request;
import com.example.springboot.common.mvc.domain.Response;

public interface Handler {

	void execute(Request request, Response response) throws Throwable;
}
