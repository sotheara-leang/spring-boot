package com.example.springboot.common.mvc.aop.aspect;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import com.example.springboot.common.mvc.filter.BasicFilterChain;
import com.example.springboot.common.mvc.filter.Filter;
import com.example.springboot.common.mvc.filter.FilterChain;
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
	
	class MethodInvocationFilter implements Filter {

		@Override
		public void doFilter( Message<Object> request, Message<Object> response, FilterChain filterChain ) throws Throwable {
			if (request == null || (request.getBody() != null 
					&& MethodInvocationProceedingJoinPoint.class.isAssignableFrom( request.getBody().getClass() ))) {
				MethodInvocationProceedingJoinPoint jp = (MethodInvocationProceedingJoinPoint) request.getBody();
				response.setBody( jp.proceed() );
			} else {
				filterChain.doFilter( request, response );
			}
		}
	}
}
