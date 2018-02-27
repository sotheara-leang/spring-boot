package com.example.springboot.common.dispatcher.aop.aspect;

import java.lang.reflect.Method;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.dispatcher.filter.Filter;
import com.example.springboot.common.dispatcher.filter.SingleThreadFilterChain;
import com.example.springboot.common.dispatcher.model.Message;
import com.example.springboot.common.dispatcher.model.MethodInvocation;

public class MethodAroundAspect {
	
	private static Logger logger = LoggerFactory.getLogger(MethodAroundAspect.class);
	
	protected List<Filter> filterList;
	
	public MethodAroundAspect(List<Filter> filterList) {
		this.filterList = filterList;
	}

	public Object aroundMethod(ProceedingJoinPoint jp) throws Throwable {
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Method method = signature.getMethod();
		
		MethodInvocation methodInvocation = new MethodInvocation(jp.getTarget(), method, jp.getArgs());
		Message request = new Message();
		request.setBody(methodInvocation);
		
		Message response;
		
		try {
			SingleThreadFilterChain filterChain = new SingleThreadFilterChain( filterList );
			response = filterChain.doFilter(request);
		} catch (Exception e) {
			logger.error( "Error apply method around aspect: {}", methodInvocation );
			throw e;
		}
		
		return response.getBody();
	}
}
