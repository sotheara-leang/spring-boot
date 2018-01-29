package com.example.springboot.common;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springboot.AbstractServiceTest;
import com.example.springboot.common.mvc.FrontHandler;
import com.example.springboot.common.mvc.model.Message;
import com.example.springboot.common.mvc.model.MessageHeaders;
import com.example.springboot.dto.MyDto;

public class LocalMvcTest extends AbstractServiceTest {

	@Autowired
	private FrontHandler frontHandler;
	
	@Test
	public void testHandle1() throws Throwable {
		MessageHeaders headers = new MessageHeaders();
		headers.put( MessageHeaders.REQ_PATH, "/handle1" );
		
		Message request = new Message(headers, new MyDto());
		
		Message response = frontHandler.execute( request );
		
		System.out.println( request );
		System.out.println( response );
	}
	
	@Test
	public void testHandle2() throws Throwable {
		MessageHeaders headers = new MessageHeaders();
		headers.put( MessageHeaders.REQ_PATH, "/handle2" );
		
		Message request = new Message(headers, new MyDto());
		
		Message response = frontHandler.execute( request );
		
		System.out.println( request );
		System.out.println( response );
	}
	
	@Test
	public void testTestUnknownPath() throws Throwable {
		MessageHeaders headers = new MessageHeaders();
		headers.put( MessageHeaders.REQ_PATH, "/unknown" );
		
		Message request = new Message(headers, new MyDto());
		
		Message response = frontHandler.execute( request );
		
		System.out.println( request );
		System.out.println( response );
	}
	
	@Test
	public void testTestAnnotation() throws Throwable {
		MessageHeaders headers = new MessageHeaders();
		headers.put( MessageHeaders.REQ_PATH, "/handle3" );
		headers.put( "myHeader", "Hello" );
		headers.put( "intHeader", 2 );
		
		Message request = new Message(headers, new MyDto());
		
		Message response = frontHandler.execute( request );
		
		System.out.println( request );
		System.out.println( response );
	}
}
