package com.example.springboot.filterchain;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springboot.common.invocation.BasicFilterChain;
import com.example.springboot.common.invocation.FilterChain;
import com.example.springboot.common.invocation.Holder;

@Aspect
@Component
public class ServiceAop {
	
	private List<ServiceFilter> serviceFilterList;
	
	@Autowired
	public ServiceAop(List<ServiceFilter> serviceFilterList) {
		this.serviceFilterList = serviceFilterList;
	}

	@Around("execution (* com.example.springboot.service..*(..))")
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
		
		filterChain.doFilter( new Holder(jointPoint), response );
		
		return response.getData();
	}
}
