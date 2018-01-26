package com.example.springboot.service.filter;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springboot.common.mvc.filter.BasicFilterChain;
import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.common.mvc.model.Message;

@Aspect
@Component
public class ServiceFilterAspect {
	
	private List<ServiceFilter> serviceFilterList;
	
	@Autowired
	public ServiceFilterAspect(List<ServiceFilter> serviceFilterList) {
		this.serviceFilterList = serviceFilterList;
	}

	@Around("execution (* com.example.springboot.service.api..*(..))")
	public Object aroundMethods( ProceedingJoinPoint jointPoint ) throws Throwable {
		ServiceFilter filter = new ServiceFilter() {
			
			@Override
			public void doFilter(Message<Object> request, Message<Object> response, FilterChain filterChain) throws Throwable {
				ProceedingJoinPoint jp = (ProceedingJoinPoint) request.getBody();
				response.setBody( jp.proceed() );
			}
		};
		serviceFilterList.add(filter);
		
		BasicFilterChain filterChain = new BasicFilterChain(serviceFilterList);
		
		Message<Object> response = new Message<Object>();
		
		filterChain.doFilter(new Message<Object>(jointPoint), response);
		
		return response.getBody();
	}
}
