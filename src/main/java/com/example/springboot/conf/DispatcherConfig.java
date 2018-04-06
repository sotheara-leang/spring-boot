package com.example.springboot.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.springboot.common.dispatcher.BasicDispatcher;
import com.example.springboot.common.dispatcher.Dispatcher;
import com.example.springboot.common.dispatcher.filter.LoggingFilter;

@Configuration
public class DispatcherConfig {
	
	@Bean
	public LoggingFilter loggingFilter() {
		return new LoggingFilter();
	}
	
	@Bean
	public Dispatcher dispatcher( ) {
		return new BasicDispatcher();
	}
}
