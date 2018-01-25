package com.example.springboot.filter;

import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.springboot.common.mvc.domain.Request;
import com.example.springboot.common.mvc.domain.Response;
import com.example.springboot.common.mvc.filler.BasicFilterChain;

@Aspect
@Component
public class ServiceAop {
	
	private List<ServiceFilter> serviceFilterList;
	
	@Autowired
	public ServiceAop(List<ServiceFilter> serviceFilterList) {
		this.serviceFilterList = serviceFilterList;
	}

	@Around("execution (* com.example.springboot.service..*(*))")
	public void aroundMethods( ProceedingJoinPoint pjp ) throws Throwable {
		MethodInvocationProceedingJoinPoint jointPoint = (MethodInvocationProceedingJoinPoint) pjp;
		
		BasicFilterChain filterChain = new BasicFilterChain(serviceFilterList);
		filterChain.setHandler( (req, res) -> {
			MethodInvocationProceedingJoinPoint invoker = (MethodInvocationProceedingJoinPoint) req.getBody();
			res.setBody( invoker.proceed() );
		} );
		
		Request request = new Request();
		request.setBody( jointPoint );
		
		Response response = new Response();
		
		filterChain.doFilter( request, response );
	}
}
