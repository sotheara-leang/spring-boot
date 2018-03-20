package com.example.springboot.common.dispatcher;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import com.example.springboot.common.dispatcher.annotation.Controller;
import com.example.springboot.common.dispatcher.annotation.RequestMapping;
import com.example.springboot.common.dispatcher.exception.HandlerNotFoundException;
import com.example.springboot.common.dispatcher.exception.MessageInvalidException;
import com.example.springboot.common.dispatcher.model.HandlingMappingInfo;
import com.example.springboot.common.dispatcher.model.MethodParameter;
import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;
import com.example.springboot.common.dispatcher.resolver.HandlerMethodParameterResolver;

public class BasicDispatcher implements ApplicationContextAware, InitializingBean, Dispatcher {

	private static Logger logger = LoggerFactory.getLogger( BasicDispatcher.class );

	protected ApplicationContext applicationContext;

	protected Map<String, HandlingMappingInfo> handlingMappingInfoMap = new HashMap<String, HandlingMappingInfo>();

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
		Map<String, Object> handlerMap = applicationContext.getBeansWithAnnotation( Controller.class );
		
		Set<Entry<String, Object>> entrySet = handlerMap.entrySet();
		for (Entry<String, Object> entry : entrySet) {
			Object handler = entry.getValue();
			
			Class<? extends Object> handlerClass = handler.getClass();
			String handlerClassName = handlerClass.getName();

			List<Method> methods = MethodUtils.getMethodsListWithAnnotation( handlerClass, RequestMapping.class );
			if ( methods != null ) {
				for ( Method method : methods ) {
					String methodName = method.getName();
					int modifiers = method.getModifiers();

					if ( Modifier.isPublic( modifiers ) && !Modifier.isAbstract( modifiers ) ) {
						RequestMapping requestMapping = method.getAnnotation( RequestMapping.class );

						String path = requestMapping.value();
						if ( StringUtils.isBlank( path ) ) {
							logger.error( "@RequestMapping invalid. Path is blank: {}.{}", handlerClassName, methodName );
							continue;
						}

						if ( handlingMappingInfoMap.get( path ) != null ) {
							logger.error( "@RequestMapping invalid. Path is duplicated: {}.{}", handlerClassName,
									methodName );
							continue;
						}

						logger.info( "Register handler method: {}.{}", handlerClassName, methodName );

						HandlingMappingInfo handlingMappingInfo = new HandlingMappingInfo();
						handlingMappingInfo.setPath( path );
						handlingMappingInfo.setMethod( method );
						handlingMappingInfo.setHandler( handler );

						handlingMappingInfoMap.put( path, handlingMappingInfo );
					}
				}
			}
		}
	}

	@Override
	public Response process( Request request ) throws Throwable {
		Response response = new Response();
		
		// validation
		String requestPath = request.getPath();
		if ( StringUtils.isBlank( requestPath ) ) {
			logger.warn( "Request path is undefined - Skip message : {}", request );
			throw new MessageInvalidException("Request url is undefined");
		}

		HandlingMappingInfo handlingMappingInfo = handlingMappingInfoMap.get( requestPath );
		if ( handlingMappingInfo != null ) {
			Object handler = handlingMappingInfo.getHandler();
			Method method = handlingMappingInfo.getMethod();

			// prepare handler parameters
			
			List<Object> parameters = new ArrayList<Object>();
			
			Parameter[] methodParamters = method.getParameters();
			if ( methodParamters != null ) {
				ParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
				String[] parameterNames = discoverer.getParameterNames( method );
				
				for ( int i = 0; i < methodParamters.length; i++ ) {
					Parameter parameter = methodParamters[i];
					String paramName = parameterNames[i];
					Class<?> paramType = parameter.getType();
					
					if ( Request.class.isAssignableFrom( paramType ) ) {
						parameters.add( request );
						continue;
					}
					
					MethodParameter methodParameter = new MethodParameter();
					methodParameter.setName( paramName );
					methodParameter.setType( paramType );
					methodParameter.setIndex( i );
					methodParameter.setAnnotations( parameter.getAnnotations() );
					
					// define parameter generic type
					Type paramGenericType = parameter.getParameterizedType();
					if (paramGenericType instanceof ParameterizedType) {
						ParameterizedType paramParameterizedType = (ParameterizedType) paramGenericType;
						Type[] actualTypeArguments = paramParameterizedType.getActualTypeArguments();
						
						methodParameter.setGenericTypes( actualTypeArguments );
					}
					
					// filter parameter
					Object resolvedParamter = null;
					if ( parameterResolvers != null ) {
						for ( HandlerMethodParameterResolver resolver : parameterResolvers ) {
							if ( resolver.supportsParameter( methodParameter ) ) {
								try {
									resolvedParamter = resolver.resolveParemeter( methodParameter, request );
								} catch ( Exception e ) {
									logger.error( "Error resolve parameter: {}, {}", methodParameter, request, e );
								}
								break;
							}
						}
					}
					parameters.add( resolvedParamter );
				}
			}
			
			logger.info( "Invoke {}.{} with paramters: {}", handler.getClass().getName(), method.getName(), parameters );
			
			// invoke handler
			Object returnValue = null;
			try {
				returnValue = method.invoke( handler, parameters.toArray() );
			} catch ( Exception e ) {
				logger.error( "Error handler invocation: {}", request );
				throw e;
			}
			response = prepareResponse( returnValue );
			
		} else {
			logger.error( "Unknown request path: {}", request);
			throw new HandlerNotFoundException( "Unknown request path: " + requestPath );
		}
		
		return response;
	}
	
	protected Response prepareResponse(Object returnValue) {
		Response response;
		if ( returnValue != null && Response.class.isAssignableFrom( returnValue.getClass() ) ) {
			response = (Response) returnValue;
		} else {
			response = new Response();
			response.setBody( returnValue );
		}
		return response;
	}

	public List<HandlerMethodParameterResolver> getParameterResolvers() {
		return parameterResolvers;
	}

	public void setParameterResolvers( List<HandlerMethodParameterResolver> parameterResolvers ) {
		this.parameterResolvers = parameterResolvers;
	}
}
