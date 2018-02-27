package com.example.springboot.common.dispatcher;

import com.example.springboot.common.dispatcher.model.Message;

public interface Dispatcher {

	Message dispatch(Message request) throws Throwable;
}
