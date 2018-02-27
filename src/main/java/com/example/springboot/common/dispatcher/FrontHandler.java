package com.example.springboot.common.mvc;

import com.example.springboot.common.mvc.model.Message;

public interface FrontHandler {

	Message execute(Message request) throws Throwable;
}
