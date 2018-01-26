package com.example.springboot.service.filter;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springboot.common.mvc.Holder;
import com.example.springboot.common.mvc.filter.BasicFilterChain;
import com.example.springboot.common.mvc.filter.FilterChain;

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
			public void doFilter(Holder request, Holder response, FilterChain filterChain) throws Throwable {
				ProceedingJoinPoint jp = (ProceedingJoinPoint) request.getData();
				Object proceed = jp.proceed();
				
				response.setData(proceed);
			}
		};
		serviceFilterList.add(filter);
		
		BasicFilterChain filterChain = new BasicFilterChain(serviceFilterList);
		
		Holder response = new Holder();
		
		filterChain.doFilter(new Holder(jointPoint), response);
		
		return response.getData();
	}
}
