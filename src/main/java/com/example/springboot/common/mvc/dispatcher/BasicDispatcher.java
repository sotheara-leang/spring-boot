package com.example.springboot.common.mvc.dispatcher;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.common.mvc.handler.Handler;
import com.example.springboot.common.mvc.model.Message;

public class BasicDispatcher implements Dispatcher {

	private static Logger logger = LoggerFactory.getLogger(BasicDispatcher.class);
	
	private FilterChain filterChain;
	
	private Map<String, Handler> handlerMap;
	
	public BasicDispatcher(List<? extends Handler> handlerList) {
		
	}
	
	@Override
	public void dispatch(Message<?> request, Message<?> response) {
		
	}

	public FilterChain getFilterChain() {
		return filterChain;
	}

	public void setFilterChain(FilterChain filterChain) {
		this.filterChain = filterChain;
	}
}
