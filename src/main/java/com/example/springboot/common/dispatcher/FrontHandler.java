package com.example.springboot.common.dispatcher;

import com.example.springboot.common.dispatcher.model.Message;

public interface FrontHandler {

	Message execute(Message request) throws Throwable;
}
