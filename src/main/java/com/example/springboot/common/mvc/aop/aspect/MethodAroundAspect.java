package com.example.springboot.common.mvc.aop.aspect;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import com.example.springboot.common.mvc.filter.BasicFilterChain;
import com.example.springboot.common.mvc.filter.Filter;
import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.common.mvc.model.Message;
import com.example.springboot.common.mvc.model.MethodInvocation;

public class MethodAroundAspect {
	
	private List<Filter> filterList;
	
	public MethodAroundAspect(List<Filter> filterList) {
		this.filterList = filterList;
	}

	public Object aroundMethod(ProceedingJoinPoint jp) throws Throwable {
		List<Filter> internalFilterList = new ArrayList<Filter>( filterList );;
		internalFilterList.add(new MethodInvocationFilter());
		
		BasicFilterChain filterChain = new BasicFilterChain(internalFilterList);
		
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Method method = signature.getMethod();
		
		Message request = new Message();
		request.setBody(new MethodInvocation(jp.getTarget(), method, jp.getArgs()));
		
		Message response = new Message();
		
		filterChain.doFilter(request, response);
		
		return response.getBody();
	}
	
	class MethodInvocationFilter implements Filter {

		@Override
		public void doFilter( Message request, Message response, FilterChain filterChain ) throws Throwable {
			if (request == null || (request.getBody() != null 
					&& MethodInvocation.class.isAssignableFrom( request.getBody().getClass() ))) {
				
				MethodInvocation methodInvocation = (MethodInvocation) request.getBody();
				response.setBody( methodInvocation.proceed() );
			} else {
				filterChain.doFilter( request, response );
			}
		}
	}
}
