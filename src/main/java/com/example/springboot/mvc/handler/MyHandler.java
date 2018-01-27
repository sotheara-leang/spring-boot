package com.example.springboot.mvc.handler;

import com.example.springboot.common.mvc.annotation.Handler;
import com.example.springboot.common.mvc.annotation.RequestMapping;
import com.example.springboot.common.mvc.model.Message;
import com.example.springboot.dto.MyDto;

@Handler
public class MyHandler {

	@RequestMapping(path = "/handle1", accept = MyDto.class)
	public Message handle1( Message request ) {
		Message response = new Message();
		response.setBody( "Success handle 1 request" );
		return response;
	}
	
	@RequestMapping(path = "/handle2", accept = MyDto.class)
	public Object handle2( Message request , Object user) {
		return "Success handle 2 request";
	}
}
