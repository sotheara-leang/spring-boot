package com.example.springboot.conf;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springboot.common.mvc.BasicFrontHandler;
import com.example.springboot.common.mvc.FrontHandler;
import com.example.springboot.common.mvc.dispatcher.BasicDispatcher;
import com.example.springboot.common.mvc.dispatcher.Dispatcher;
import com.example.springboot.common.mvc.filter.BasicFilterChain;
import com.example.springboot.common.mvc.filter.Filter;
import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.common.mvc.filter.LoggingFilter;
import com.example.springboot.common.mvc.resolver.BodyParameterResolver;
import com.example.springboot.common.mvc.resolver.HandlerMethodParameterResolver;
import com.example.springboot.common.mvc.resolver.HeaderParameterResolver;

@Configuration
public class LocalMvcConfig {

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
	public FilterChain filterChain(List<Filter> filterList) {
		return new BasicFilterChain( filterList );
	}
	
	@Bean
	public LoggingFilter loggingFilter() {
		return new LoggingFilter();
	}
	
	@Bean
	public FrontHandler frontHandler(FilterChain filterChain, Dispatcher dispatcher) {
		return new BasicFrontHandler( filterChain, dispatcher );
	}
}
