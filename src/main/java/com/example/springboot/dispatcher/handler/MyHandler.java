package com.example.springboot.dispatcher.handler;

import javax.validation.constraints.NotNull;

import com.example.springboot.common.dispatcher.annotation.Body;
import com.example.springboot.common.dispatcher.annotation.Controller;
import com.example.springboot.common.dispatcher.annotation.Header;
import com.example.springboot.common.dispatcher.annotation.RequestMapping;
import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;
import com.example.springboot.dto.MyDto;

@Controller
public class MyHandler {

	@RequestMapping("/handle1")
	public Response handle1( Request request ) {
		Response response = new Response();
		response.setBody( "Success handle 1 request" );
		return response;
	}
	
	@RequestMapping("/handle2")
	public Object handle2( Request request ) {
		return "Success handle 2 request";
	}
	
	@RequestMapping("/handle3")
	public Object handle3( @NotNull @Header String myHeader, @Header("intHeader") Integer customHeader, @Body MyDto myDto ) throws Exception {
		throw new Exception( "" );
//		return "Success handle 2 request";
	}
}
