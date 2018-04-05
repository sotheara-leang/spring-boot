package com.example.springboot.conf;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springboot.common.dispatcher.BasicDispatcher;
import com.example.springboot.common.dispatcher.Dispatcher;
import com.example.springboot.common.dispatcher.exception.ExceptionHandler;
import com.example.springboot.common.dispatcher.filter.ExceptionHandlerFilter;
import com.example.springboot.common.dispatcher.filter.Filter;
import com.example.springboot.common.dispatcher.filter.LoggingFilter;
import com.example.springboot.dispatcher.exception.MyExceptionHandler;

@Configuration
public class DispatcherConfig {
	
	@Bean
	public LoggingFilter loggingFilter() {
		return new LoggingFilter();
	}
	
	@Bean
	public MyExceptionHandler myExceptionHandler() {
		return new MyExceptionHandler();
	}
	
	@SuppressWarnings( "rawtypes" )
	@Bean
	public ExceptionHandlerFilter exceptionHandlerFilter( List<ExceptionHandler> exceptionHandlers ) {
		return new ExceptionHandlerFilter( exceptionHandlers );
	}
	
	@Bean
	public Dispatcher dispatcher( List<Filter> filters ) {
		BasicDispatcher dispactcher = new BasicDispatcher();
		dispactcher.setFilters( filters );
		return dispactcher;
	}
}
