package com.example.springboot.common.mvc.handler;

import com.example.springboot.common.mvc.model.Message;

public interface ReturnHandler<T> extends Handler {

	Object execute(Message<T> request);
}
