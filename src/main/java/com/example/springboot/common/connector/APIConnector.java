package com.example.springboot.common.connector;

import java.io.File;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.Set;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class APIConnector {
	
	private static final Logger logger = LoggerFactory.getLogger(APIConnector.class);
	
	private static final ObjectMapper objectMapper = new ObjectMapper()
			.setNodeFactory( JsonNodeFactory.withExactBigDecimals(true) )
			.setSerializationInclusion( Include.NON_NULL )
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		
	private String baseUrl;
	
	private RestOperations restOperations;
	
	public APIConnector(RestTemplate restTemplate) {
		this("", restTemplate);
	}
	
	public APIConnector(String baseUrl, RestOperations restOperations) {
		this.baseUrl = baseUrl;
		this.restOperations = restOperations;
	}

	// GET
	
	public <RES> RES get(String url, Object request, Class<RES> responseClass) throws RestClientException, URISyntaxException {
		return request(url, HttpMethod.GET, request, responseClass);
	}
	
	public <RES> RES get(String url, Object request, ParameterizedTypeReference<RES> responseType) throws URISyntaxException, RestClientException {
		return request(url, HttpMethod.GET, request, responseType);
	}
	
	// POST
	
	public <RES> RES post(String url, Object request, Class<RES> responseClass) throws RestClientException, URISyntaxException {
		return request(url, HttpMethod.POST, responseClass);
	}
	
	public <RES> RES post(String url, Object request, ParameterizedTypeReference<RES> responseType) throws URISyntaxException, RestClientException {
		return request(url, HttpMethod.POST, request, responseType);
	}
	
	// Request
	
	public <RES> RES request(String url, HttpMethod method, Class<RES> responseClass) throws RestClientException, URISyntaxException {
		return request(url, method, null, responseClass);
	}
	
	public <RES> RES request(String url, HttpMethod method, Object request, Class<RES> responseClass) throws RestClientException, URISyntaxException {
		return request(url, method, request, new ParameterizedTypeReference<RES>() {

			@Override
			public Type getType() {
				return responseClass;
			}
		});
	}
	
	public <RES> RES request(String url, HttpMethod method, ParameterizedTypeReference<RES> responseType) throws RestClientException, URISyntaxException {
		return request(url, method, null, responseType);
	}
	
	public <RES> RES request(String url, HttpMethod method, Object request, ParameterizedTypeReference<RES> responseType) throws URISyntaxException, RestClientException {
		 return request(url, method, null, request, responseType);
	}
	
	public <RES> RES requestMultipart(String url, MultiValueMap<String, Object> requestMap, Class<RES> responseClass) throws URISyntaxException, RestClientException {
		return requestMultipart(url, requestMap, new ParameterizedTypeReference<RES>() {
			@Override
			public Type getType() {
				return responseClass;
			}
		});
	}
	
	public <RES> RES requestMultipart(String url, MultiValueMap<String, Object> requestMap, ParameterizedTypeReference<RES> responseType) throws URISyntaxException, RestClientException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		return request(url, HttpMethod.POST, headers, requestMap, responseType);
	}
	
	@SuppressWarnings("unchecked")
	public <RES> RES request(String url, HttpMethod method, HttpHeaders headers, Object request, ParameterizedTypeReference<RES> responseType) throws URISyntaxException, RestClientException {
		RES response = null;
		
		String fullUrl = baseUrl + url;
		long t1 = System.currentTimeMillis();
		
		try {
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> REQUEST TO EXTERNAL API >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			logger.debug("Url: {}", fullUrl);
			logger.debug("Headers: {}", serialize(headers));
			logger.debug("Request: {}", serialize(request));
			logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			
			Object internalRequest = request;
			
			if (headers != null) {
				if (MediaType.MULTIPART_FORM_DATA.equals(headers.getContentType()) && MultiValueMap.class.isAssignableFrom(request.getClass())) {
					MultiValueMap<String, Object> internalMultiValueMap = new LinkedMultiValueMap<String, Object>();
					
					MultiValueMap<String, Object> requestMultiValueMap = (MultiValueMap<String, Object>) request;
					Set<String> reqParamNames = requestMultiValueMap.keySet();
					
					for (String paramName : reqParamNames) {
						LinkedList<Object> paramValueList = (LinkedList<Object>) requestMultiValueMap.get(paramName);
						
						for (Object paramValue : paramValueList) {
							Object t = paramValue;
							if (paramValue != null) {
								Class<?> paramValueClass = paramValue.getClass();
								if (File.class.isAssignableFrom(paramValueClass)) {
									t = new FileSystemResource((File) paramValue);
								} 
							}
							internalMultiValueMap.add(paramName, t);
						}
					}
					
					internalRequest = internalMultiValueMap;
				}
			}
			
			RequestEntity<Object> reqEntity = new RequestEntity<Object>(internalRequest, headers, method, new URI(fullUrl));  
			ResponseEntity<RES> resEntity = restOperations.exchange(reqEntity, responseType);
			response = resEntity.getBody();
			
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< RESPONSE FROM EXTERNAL API <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			logger.debug("Status code: {}", resEntity.getStatusCode());
			logger.debug("Headers: {}", serialize(resEntity.getHeaders()));
			logger.debug("Response: {}", serialize (response));
			logger.debug("Interval time: {}ms", System.currentTimeMillis() - t1);
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			
		} catch (URISyntaxException e) {
			logger.error("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ERROR REQUEST TO EXTERNAL API <<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			logger.error("Url is incorrect: {}", fullUrl, e);
			logger.error("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			
			throw e;
			
		} catch (Exception e) {
			logger.error("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ERROR REQUEST TO EXTERNAL API <<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			
			if (e instanceof RestClientException) {
				Throwable cause = e.getCause();
				
				if (cause instanceof ConnectException) {
					logger.error("Connection refused to external API.");
				} else if (cause instanceof ConnectionPoolTimeoutException) {
					logger.error("Connection pool timeout.");
				} else if (cause instanceof ConnectTimeoutException) {
					logger.error("Connection timeout to external API.");
				} else if (cause instanceof SocketTimeoutException) {
					logger.error("Read time out to external API.");
				} else if (cause instanceof SocketException) {
					logger.error("Error creating/accessing connection.");
				} else {
					logger.error("Error while calling external API.");
				}
			} else {
				logger.error("Error while calling external API.");
			}
			
			logger.error("Interval time: {}ms", System.currentTimeMillis() - t1, e);
			logger.error("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			
			throw e;
		}
		
		return response;
	}
	
	private String serialize(Object request) {
		String json = null;
		try {
			json = objectMapper.writeValueAsString( request );
		} catch ( JsonProcessingException e ) {
			logger.error( "Error serialize request: {}", request);
		}
		return json;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public RestOperations getRestOperations() {
		return restOperations;
	}
}