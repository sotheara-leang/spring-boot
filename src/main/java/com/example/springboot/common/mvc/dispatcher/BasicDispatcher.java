package com.example.springboot.common.mvc.dispatcher;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import com.example.springboot.common.mvc.annotation.Handler;
import com.example.springboot.common.mvc.annotation.RequestMapping;
import com.example.springboot.common.mvc.filter.FilterChain;
import com.example.springboot.common.mvc.model.MappingContext;
import com.example.springboot.common.mvc.model.Message;
import com.example.springboot.common.mvc.model.MessageHeaders;
import com.example.springboot.common.mvc.model.MethodInvocation;

public class BasicDispatcher implements ApplicationContextAware, InitializingBean, Dispatcher {

	private static Logger logger = LoggerFactory.getLogger(BasicDispatcher.class);
	
	protected ApplicationContext applicationContext;
	
	protected FilterChain filterChain;
	
	private String basePackage;
	
	protected Map<String, MappingContext> mappingContextMap = new HashMap<String, MappingContext>();
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}
	
	protected void init() throws BeansException, ClassNotFoundException {
		Assert.hasText(basePackage, "base package is undefined");
	
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(true);
		scanner.addIncludeFilter(new AnnotationTypeFilter(Handler.class));
		
		Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(basePackage);
		for (BeanDefinition beanDefinition : beanDefinitions) {
			Object bean = applicationContext.getBean(Class.forName(beanDefinition.getBeanClassName()));
			
			Class<? extends Object> beanClass = bean.getClass();
			String className = beanClass.getName();
			
			Method[] methods = beanClass.getMethods();
			if (methods != null) {
				for (Method method : methods) {
					String methodName = method.getName();
					int modifiers = method.getModifiers();
					
					if (Modifier.isPublic( modifiers ) && !Modifier.isAbstract( modifiers )) {
						RequestMapping requestMapping = method.getAnnotation( RequestMapping.class );
						String path = requestMapping.path();
						if (StringUtils.isBlank( path )) {
							logger.warn( "@RequestMapping invalid. Path is blank: {}.{}",  className, methodName );
							continue;
						}
						
						if (mappingContextMap.get( path ) != null) {
							logger.warn( "@RequestMapping invalid. Path is dupplicated: {}.{}",  className, methodName );
							continue;
						}
						
						int parameterCount = method.getParameterCount();
						if (parameterCount != 1) {
							logger.warn( "Mapping method is invalid - must contain only request of type Message: {}.{}",  className, methodName );
						}
						
						Class<?> accept = requestMapping.accept();
						MethodInvocation methodInvocation = new MethodInvocation( bean, method );
						
						MappingContext mappingContext = new MappingContext();
						mappingContext.setPath( path );
						mappingContext.setAccept( accept );
						mappingContext.setMethodInvocation( methodInvocation );

						mappingContextMap.put( path, mappingContext );
					}
				}
			}
		}
	}

	@Override
	public void dispatch(Message request, Message response) throws Throwable {
		MessageHeaders headers = request.getHeaders();
		String requestUrl = headers.getRequestUrl();
		if (StringUtils.isBlank( requestUrl )) {
			logger.debug( "Request url is undefine - Skip message : {}", request );
			return;
		}
		
		MappingContext mappingContext = mappingContextMap.get( requestUrl );
		if (mappingContext != null) {
			Class<?> accept = mappingContext.getAccept();
			
			Object requestBody = request.getBody();
			if (requestBody != null && accept.isAssignableFrom( requestBody.getClass() )) {
				MethodInvocation methodInvocation = mappingContext.getMethodInvocation();
				
				Object responseBody = methodInvocation.proceed( request );
				if (Message.class.isAssignableFrom( requestBody.getClass() )) {
					Message newResponse = (Message) responseBody;
					response.setHeaders( newResponse.getHeaders() );
					response.setBody( newResponse.getBody() );
				} else {
					response.setBody( responseBody );
				}
			}
		}
	}

	public FilterChain getFilterChain() {
		return filterChain;
	}

	public void setFilterChain(FilterChain filterChain) {
		this.filterChain = filterChain;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage( String basePackage ) {
		this.basePackage = basePackage;
	}
}