package com.example.springboot.common;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springboot.AbstractServiceTest;
import com.example.springboot.common.dispatcher.FrontHandler;
import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;
import com.example.springboot.dto.MyDto;

public class DispatcherTest extends AbstractServiceTest {

	@Autowired
	private FrontHandler frontHandler;
	
	@Test
	public void testHandle1() throws Throwable {
		Request request = new Request();
		request.setPath( "/handle1" );
		
		Response response = frontHandler.process( request );
		
		System.out.println( request );
		System.out.println( response );
	}
	
	@Test
	public void testHandle2() throws Throwable {
		Request request = new Request();
		request.setPath( "/handle2" );
		request.setBody( new MyDto() );
		
		Response response = frontHandler.process( request );
		
		System.out.println( request );
		System.out.println( response );
	}
	
	@Test
	public void testTestUnknownPath() throws Throwable {
		Request request = new Request();
		request.setPath( "/unkown" );
		
		Response response = frontHandler.process( request );
		
		System.out.println( request );
		System.out.println( response );
	}
	
	@Test
	public void testTestAnnotation() throws Throwable {
		Request request = new Request();
		request.setPath( "/handle3" );
		request.setHeader( "myHeader", "Hello" );
		request.setHeader( "intHeader", 2 );
		request.setBody( new MyDto() );
		
		Response response = frontHandler.process( request );
		
		System.out.println( request );
		System.out.println( response );
	}
}
