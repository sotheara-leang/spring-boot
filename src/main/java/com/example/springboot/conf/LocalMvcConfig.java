package com.example.springboot.conf;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springboot.common.mvc.BasicFrontHandler;
import com.example.springboot.common.mvc.FrontHandler;
import com.example.springboot.common.mvc.dispatcher.BasicDispatcher;
import com.example.springboot.common.mvc.dispatcher.Dispatcher;
import com.example.springboot.common.mvc.filter.BasicFilterChain;
import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.mvc.filter.LoggingFilter;

@Configuration
public class LocalMvcConfig {

	@Bean
	public Dispatcher dispatcher() {
		BasicDispatcher dispactcher = new BasicDispatcher();
		dispactcher.setBasePackage( "com.example.springboot.mvc.handler" );
		return dispactcher;
	}
	
	@Bean
	public FilterChain filterChain() {
		BasicFilterChain filterChain = new BasicFilterChain( Arrays.asList( loggingFilter() ) );
		return filterChain;
	}
	
	@Bean
	public LoggingFilter loggingFilter() {
		return new LoggingFilter();
	}
	
	@Bean
	public FrontHandler frontHandler() {
		return new BasicFrontHandler( filterChain(), dispatcher() );
	}
}
