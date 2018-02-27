package com.example.springboot.common.dispatcher;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

import com.example.springboot.common.dispatcher.annotation.Handler;
import com.example.springboot.common.dispatcher.annotation.RequestMapping;
import com.example.springboot.common.dispatcher.exception.HandlerNotFoundException;
import com.example.springboot.common.dispatcher.exception.MessageInvalidException;
import com.example.springboot.common.dispatcher.model.MappingContext;
import com.example.springboot.common.dispatcher.model.Message;
import com.example.springboot.common.dispatcher.model.MessageHeaders;
import com.example.springboot.common.dispatcher.model.MethodInvocation;
import com.example.springboot.common.dispatcher.model.MethodParameter;
import com.example.springboot.common.dispatcher.resolver.HandlerMethodParameterResolver;

public class BasicDispatcher implements ApplicationContextAware, InitializingBean, Dispatcher {

	private static Logger logger = LoggerFactory.getLogger( BasicDispatcher.class );

	protected ApplicationContext applicationContext;

	private String basePackage;

	protected Map<String, MappingContext> mappingContextMap = new HashMap<String, MappingContext>();

	protected List<HandlerMethodParameterResolver> parameterResolvers;

	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		init();
	}

	protected void init() throws BeansException, ClassNotFoundException {
		Assert.hasText( basePackage, "base package is undefined" );

		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider( true );
		scanner.addIncludeFilter( new AnnotationTypeFilter( Handler.class ) );

		Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents( basePackage );
		for ( BeanDefinition beanDefinition : beanDefinitions ) {
			Object bean = applicationContext.getBean( Class.forName( beanDefinition.getBeanClassName() ) );

			Class<? extends Object> beanClass = bean.getClass();
			String className = beanClass.getName();

			Method[] methods = beanClass.getDeclaredMethods();
			if ( methods != null ) {
				for ( Method method : methods ) {
					String methodName = method.getName();
					int modifiers = method.getModifiers();

					if ( Modifier.isPublic( modifiers ) && !Modifier.isAbstract( modifiers ) ) {
						RequestMapping requestMapping = method.getAnnotation( RequestMapping.class );
						if ( requestMapping == null ) {
							logger.error( "@RequestMapping not found: {}.{}", className, methodName );
							continue;
						}

						String path = requestMapping.path();
						if ( StringUtils.isBlank( path ) ) {
							logger.error( "@RequestMapping invalid. Path is blank: {}.{}", className, methodName );
							continue;
						}

						if ( mappingContextMap.get( path ) != null ) {
							logger.error( "@RequestMapping invalid. Path is dupplicated: {}.{}", className,
									methodName );
							continue;
						}

						logger.info( "Register handler method: {}.{}", className, methodName );

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
	public Message dispatch( Message request ) throws Throwable {
		Message response = new Message();
		
		MessageHeaders headers = request.getHeaders();
		String requestUrl = headers.getRequestUrl();
		if ( StringUtils.isBlank( requestUrl ) ) {
			logger.warn( "Request url is undefined - Skip message : {}", request );
			throw new MessageInvalidException("Request url is undefined");
		}

		MappingContext mappingContext = mappingContextMap.get( requestUrl );
		if ( mappingContext != null ) {
			Class<?> accept = mappingContext.getAccept();

			MethodInvocation methodInvocation = mappingContext.getMethodInvocation();
			Object handler = methodInvocation.getTarget();
			Method method = methodInvocation.getMethod();

			Object requestBody = request.getBody();
			if ( requestBody == null || accept.isAssignableFrom( requestBody.getClass() ) ) {
				logger.info( "Forward {} to {}.{}", request, handler.getClass().getName(), method.getName() );

				List<Object> filterParameters = new ArrayList<>();

				Parameter[] parameters = method.getParameters();
				if ( parameters != null ) {
					ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
					String[] parameterNames = discoverer.getParameterNames( method );
					
					for ( int i = 0; i < parameters.length; i++ ) {
						Parameter parameter = parameters[i];
						String paramName = parameterNames[i];
						Class<?> paramType = parameter.getType();
						
						if (Message.class.isAssignableFrom( paramType )) {
							filterParameters.add( request );
							continue;
						}
						
						MethodParameter methodParameter = new MethodParameter();
						methodParameter.setParameterName( paramName );
						methodParameter.setParameterType( paramType );
						methodParameter.setParameterIndex( i );
						methodParameter.setParameterAnnotations( parameter.getAnnotations() );
						
						Object filterParameter = null;
						if ( parameterResolvers != null ) {
							for ( HandlerMethodParameterResolver resolver : parameterResolvers ) {
								if ( resolver.supportsParameter( methodParameter ) ) {
									try {
										filterParameter = resolver.resolveParemeter( methodParameter, request );
									} catch ( Exception e ) {
										logger.error( "Error resolve parameter: {}", parameter, e );
									}
									break;
								}
							}
						}
						filterParameters.add( filterParameter );
					}
				}

				Object returnValue = null;
				try {
					returnValue = methodInvocation.proceed( filterParameters.toArray() );
				} catch ( Exception e ) {
					logger.error( "Error handle request message: {}", request );
					throw e;
				}
				
				if ( Message.class.isAssignableFrom( returnValue.getClass() ) ) {
					Message newResponse = (Message) returnValue;
					response.setHeaders( newResponse.getHeaders() );
					response.setBody( newResponse.getBody() );
				} else {
					response.setBody( returnValue );
				}
			} else {
				logger.error( "Reject {} to {}.{}", request, handler.getClass().getName(), method.getName() );
				throw new MessageInvalidException("Request body not support");
			}
		} else {
			logger.error( "Unknown request url: {}", request);
			throw new HandlerNotFoundException( "Unknown request url: " + requestUrl );
		}
		
		return response;
	}

	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage( String basePackage ) {
		this.basePackage = basePackage;
	}

	public List<HandlerMethodParameterResolver> getParameterResolvers() {
		return parameterResolvers;
	}

	public void setParameterResolvers( List<HandlerMethodParameterResolver> parameterResolvers ) {
		this.parameterResolvers = parameterResolvers;
	}
}
