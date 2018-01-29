package com.example.springboot.common.mvc.aop.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.common.mvc.model.Message;
import com.example.springboot.common.mvc.model.MethodInvocation;

public class MethodAroundAspect {
	
	private static Logger logger = LoggerFactory.getLogger(MethodAroundAspect.class);
	
	private FilterChain filterChain;
	
	public MethodAroundAspect(FilterChain filterChain) {
		this.filterChain = filterChain;
	}

	public Object aroundMethod(ProceedingJoinPoint jp) throws Throwable {
		MethodSignature signature = (MethodSignature) jp.getSignature();
		Method method = signature.getMethod();
		
		MethodInvocation methodInvocation = new MethodInvocation(jp.getTarget(), method, jp.getArgs());
		Message request = new Message();
		request.setBody(methodInvocation);
		
		Message response = new Message();
		
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			logger.error( "Error apply method around aspect: {}", methodInvocation );
			throw e;
		}
		
		return response.getBody();
	}
}
