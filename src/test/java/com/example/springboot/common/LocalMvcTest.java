package com.example.springboot.common;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springboot.AbstractServiceTest;
import com.example.springboot.common.mvc.FrontHandler;
import com.example.springboot.common.mvc.model.Message;
import com.example.springboot.common.mvc.model.MessageHeaders;
import com.example.springboot.dto.UserDto;

public class LocalMvcTest extends AbstractServiceTest {

	@Autowired
	private FrontHandler frontHandler;
	
	@Test
	public void testDispatcher() throws Throwable {
		MessageHeaders headers = new MessageHeaders();
		headers.put( MessageHeaders.REQ_PATH, "/myHandler" );
		
		UserDto userDto = new UserDto();
		
		Message request = new Message(headers, userDto);
		
		frontHandler.execute( request );
	}
}
