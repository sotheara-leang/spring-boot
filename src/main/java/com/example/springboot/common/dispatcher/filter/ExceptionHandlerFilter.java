package com.example.springboot.common.dispatcher.filter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.springboot.common.dispatcher.exception.ExceptionHandler;
import com.example.springboot.common.dispatcher.model.Request;
import com.example.springboot.common.dispatcher.model.Response;

@SuppressWarnings( "rawtypes" )
public class ExceptionHandlerFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger( ExceptionHandlerFilter.class );
	
	private List<ExceptionHandler> exceptionHandlers = new ArrayList<ExceptionHandler>();
	
	public ExceptionHandlerFilter( List<ExceptionHandler> exceptionHandlers ) {
		this.exceptionHandlers = exceptionHandlers;
	}

	@SuppressWarnings( "unchecked" )
	@Override
	public Response doFilter( Request request, FilterChain filterChain ) throws Throwable {
		Response response = null;
		
		try {
			response = filterChain.doFilter( request );
		} catch ( Exception exception ) {
			logger.error( "Error process request: {}", request );
			
			for ( ExceptionHandler exceptionHandler : exceptionHandlers ) {
				Type type = exceptionHandler.getClass().getGenericInterfaces()[0];
				if ( type instanceof ParameterizedType ) {
					
					ParameterizedType parameterizedType = (ParameterizedType) type;
					Class<?> exceptionClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
					if ( exceptionClass != null && exceptionClass.isAssignableFrom( exception.getClass() ) ) {
						Object result = exceptionHandler.resolveException( request, exception );
						response = prepareResponse( result );
					}
				}
			}
			
			if ( response == null ) {
				throw exception;
			}
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
}
