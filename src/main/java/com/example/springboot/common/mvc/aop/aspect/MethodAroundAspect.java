package com.example.springboot.common.mvc.aop.aspect;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;

import com.example.springboot.common.mvc.filter.BasicFilterChain;
import com.example.springboot.common.mvc.filter.Filter;
import com.example.springboot.common.mvc.filter.MethodInvocationFilter;
import com.example.springboot.common.mvc.model.Message;

public class MethodAroundAspect {
	
	private List<Filter> filterList;
	
	public MethodAroundAspect(List<Filter> filterList) {
		this.filterList = filterList;
	}

	public Object aroundMethod(ProceedingJoinPoint jointPoint) throws Throwable {
		List<Filter> internalFilterList = new ArrayList<Filter>( filterList );;
		internalFilterList.add(new MethodInvocationFilter());
		
		BasicFilterChain filterChain = new BasicFilterChain(internalFilterList);
		
		Message<Object> response = new Message<Object>();
		
		filterChain.doFilter(new Message<Object>(jointPoint), response);
		
		return response.getBody();
	}
}
