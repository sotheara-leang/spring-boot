package com.example.springboot.common.mvc.handler;

import com.example.springboot.common.mvc.model.Message;

public interface VoidHandler<T> extends Handler {

	void execute(Message<T> request);
}
