package com.example.springboot.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springboot.common.dispatcher.BasicDispatcher;
import com.example.springboot.common.dispatcher.BasicFrontHandler;
import com.example.springboot.common.dispatcher.Dispatcher;
import com.example.springboot.common.dispatcher.FrontHandler;
import com.example.springboot.common.dispatcher.filter.Filter;
import com.example.springboot.common.dispatcher.filter.LoggingFilter;
import com.example.springboot.common.dispatcher.resolver.BodyParameterResolver;
import com.example.springboot.common.dispatcher.resolver.HandlerMethodParameterResolver;
import com.example.springboot.common.dispatcher.resolver.HeaderParameterResolver;

@Configuration
public class DispatcherConfig {

	@Bean
	public Dispatcher dispatcher() {
		List<HandlerMethodParameterResolver> parameterResolvers = new ArrayList<HandlerMethodParameterResolver>();
		parameterResolvers.add( new HeaderParameterResolver());
		parameterResolvers.add( new BodyParameterResolver() );
		
		BasicDispatcher dispactcher = new BasicDispatcher();
		dispactcher.setParameterResolvers( parameterResolvers );
		dispactcher.setBasePackage( "com.example.springboot.mvc.handler" );
		return dispactcher;
	}
	
	@Bean
	public LoggingFilter loggingFilter() {
		return new LoggingFilter();
	}
	
	@Bean
	public FrontHandler frontHandler(List<Filter> filterList, Dispatcher dispatcher) {
		return new BasicFrontHandler( filterList, dispatcher );
	}
}
