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

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;

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
import com.example.springboot.common.dispatcher.exception.ParameterInvalidException;
import com.example.springboot.common.dispatcher.exception.RequestInvalidException;
import com.example.springboot.common.dispatcher.filter.Filter;
import com.example.springboot.common.dispatcher.filter.FilterChain;
import com.example.springboot.common.dispatcher.filter.SingleThreadFilterChain;
import com.example.springboot.common.dispatcher.interceptor.Interceptor;
import com.example.springboot.common.dispatcher.model.HandlingMappingInfo;
import com.example.springboot.common.dispatcher.model.Headers;
import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;
import com.example.springboot.common.dispatcher.resolver.BodyParameterResolver;
import com.example.springboot.common.dispatcher.resolver.HeaderParameterResolver;
import com.example.springboot.common.dispatcher.resolver.ParameterResolver;
import com.example.springboot.common.util.CollectionUtils;

public class BasicDispatcher implements ApplicationContextAware, InitializingBean, Dispatcher {

	private static Logger logger = LoggerFactory.getLogger( BasicDispatcher.class );

	protected ApplicationContext applicationContext;

	protected Map<String, HandlingMappingInfo> handlingMappingInfoMap = new HashMap<String, HandlingMappingInfo>();
	
	protected List<Filter> filters = new ArrayList<Filter>();

	protected List<ParameterResolver> parameterResolvers = new ArrayList<ParameterResolver>();
	
	protected List<Interceptor> interceptors = new ArrayList<Interceptor>();
	
	protected Validator validator;

	@Override
	public void setApplicationContext( ApplicationContext applicationContext ) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		initHandlingMappingInfo();
		
		initValidator();
		
		initParameterResolvers();
	}

	protected void initHandlingMappingInfo() throws BeansException, ClassNotFoundException {
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
	
	protected void initValidator() {
		if ( validator == null ) {
			validator = Validation.buildDefaultValidatorFactory().getValidator();
		}
	}
	
	protected void initParameterResolvers( ) {
		if ( !CollectionUtils.existOfType( parameterResolvers, HeaderParameterResolver.class ) ) {
			parameterResolvers.add( new HeaderParameterResolver() );
		}
		if ( !CollectionUtils.existOfType( parameterResolvers, BodyParameterResolver.class ) ) {
			parameterResolvers.add( new BodyParameterResolver() );
		}
	}

	@Override
	public Response process( Request request ) throws Throwable {
		List<Filter> internalFilters = new ArrayList<Filter>(filters);
		
		Filter lastFilter = null;
		if (internalFilters.size() > 0) {
			lastFilter = internalFilters.get( internalFilters.size() - 1 );
		}
		
		if (lastFilter == null || !DispatchFilter.class.isAssignableFrom( lastFilter.getClass() )) {
			lastFilter = new DispatchFilter();
			internalFilters.add( lastFilter );
		}
		
		FilterChain internalFilterChain = new SingleThreadFilterChain( internalFilters );
		return internalFilterChain.doFilter( request );
	}
	
	protected Response doDispatch( Request request ) throws Throwable {
		Response response = new Response();
		
		// validation
		String requestPath = request.getPath();
		if ( StringUtils.isBlank( requestPath ) ) {
			logger.error( "Request path is undefined - Skip message : {}", request );
			throw new RequestInvalidException("Path is undefined");
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
					Parameter param = methodParamters[i];
					String paramName = parameterNames[i];
					Class<?> paramType = param.getType();
					
					// default parameters
					if ( Request.class.isAssignableFrom( paramType ) ) {
						parameters.add( request );
						continue;
					}
					
					if ( Headers.class.isAssignableFrom( paramType ) ) {
						parameters.add( request.getHeaders() );
						continue;
					}
					
					com.example.springboot.common.dispatcher.model.Parameter handlingParameter = new com.example.springboot.common.dispatcher.model.Parameter();
					handlingParameter.setName( paramName );
					handlingParameter.setType( paramType );
					handlingParameter.setIndex( i );
					handlingParameter.setAnnotations( param.getAnnotations() );
					
					// define parameter generic type
					Type paramGenericType = param.getParameterizedType();
					if (paramGenericType instanceof ParameterizedType) {
						ParameterizedType paramParameterizedType = (ParameterizedType) paramGenericType;
						Type[] actualTypeArguments = paramParameterizedType.getActualTypeArguments();
						
						handlingParameter.setGenericTypes( actualTypeArguments );
					}
					
					// filter parameter
					Object resolvedParamter = null;
					if ( parameterResolvers != null ) {
						for ( ParameterResolver resolver : parameterResolvers ) {
							if ( resolver.supportsParameter( handlingParameter ) ) {
								try {
									resolvedParamter = resolver.resolveParameter( handlingParameter, request );
								} catch ( Exception e ) {
									logger.error( "Error resolve parameter: {}, {}", handlingParameter, request, e );
									throw new ParameterInvalidException( e );
								}
								break;
							}
						}
					}
					parameters.add( resolvedParamter );
				}
			}
			
			// validate handling parameters
			ExecutableValidator executableValidator = validator.forExecutables();
			Set<ConstraintViolation<Object>> constraintViolations = executableValidator.validateParameters( handler, method, parameters.toArray() );
			if ( !constraintViolations.isEmpty() ) {
				logger.error( "Handling parameters invalid: {}", constraintViolations );
				throw new ConstraintViolationException("Handling parameters invalid: " + constraintViolations, constraintViolations );
			}
			
			logger.info( "Invoke {}.{} with paramters: {}", handler.getClass().getName(), method.getName(), parameters );
			
			// apply interceptor pre-process
			for ( Interceptor interceptor : interceptors ) {
				boolean isAccept = interceptor.preProcess( request, response );
				if ( !isAccept ) {
					return response;
				}
			}
			
			// invoke handler
			Object returnValue = null;
			try {
				returnValue = method.invoke( handler, parameters.toArray() );
			} catch ( Exception e ) {
				logger.error( "Error handling invocation: {}", request );
				throw e;
			}
			response = prepareResponse( returnValue );
			
			// apply interceptor post-process
			for ( Interceptor interceptor : interceptors ) {
				interceptor.postProcess( request, response );
			}
			
		} else {
			logger.error( "Unknown request path: {}", request);
			throw new HandlerNotFoundException( "Unknown request path: " + requestPath );
		}
		
		return response;
	}
	
	private class DispatchFilter implements Filter {
		
		@Override
		public Response doFilter( Request request, FilterChain filterChain ) throws Throwable {
			return doDispatch( request );
		}
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

	public List<ParameterResolver> getParameterResolvers() {
		return parameterResolvers;
	}

	public void setParameterResolvers( List<ParameterResolver> parameterResolvers ) {
		this.parameterResolvers = parameterResolvers;
	}

	public List<Filter> getFilters() {
		return filters;
	}

	public void setFilters( List<Filter> filters ) {
		this.filters = filters;
	}

	public List<Interceptor> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors( List<Interceptor> interceptors ) {
		this.interceptors = interceptors;
	}

	public Validator getValidator() {
		return validator;
	}

	public void setValidator( Validator validator ) {
		this.validator = validator;
	}
}
