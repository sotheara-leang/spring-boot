package com.example.springboot.handler;

import com.example.springboot.common.mvc.annotation.Handler;
import com.example.springboot.common.mvc.annotation.RequestMapping;
import com.example.springboot.common.mvc.model.Message;
import com.example.springboot.dto.UserDto;

@Handler
public class MyHandler {

	@RequestMapping(path = "/myHandler", accept = UserDto.class)
	public void execute( Message request ) {
		
	}
}
