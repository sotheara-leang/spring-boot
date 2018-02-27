package com.example.springboot.dispatch.handler;

import com.example.springboot.common.dispatcher.annotation.Body;
import com.example.springboot.common.dispatcher.annotation.Handler;
import com.example.springboot.common.dispatcher.annotation.Header;
import com.example.springboot.common.dispatcher.annotation.RequestMapping;
import com.example.springboot.common.dispatcher.model.Message;
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
	public Object handle2( Message request ) {
		return "Success handle 2 request";
	}
	
	@RequestMapping(path = "/handle3")
	public Object handle3( @Header String myHeader, @Header("intHeader") Integer customHeader, @Body MyDto myDto ) {
		return "Success handle 2 request";
	}
}
