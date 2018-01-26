package com.example.springboot.common.mvc.dispatcher;

import com.example.springboot.common.mvc.model.Message;

public interface Dispatcher {

	void dispatch(Message<?> request, Message<?> response);
}
