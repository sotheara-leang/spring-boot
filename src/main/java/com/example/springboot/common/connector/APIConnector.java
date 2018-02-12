package com.example.springboot.common.connector;

import java.io.File;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.Arrays;
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
import org.springframework.util.Assert;
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
	
	private String baseUrl;
	
	private RestOperations restOperations;
	
	private ObjectMapper objectMapper;
	
	public APIConnector(RestTemplate restTemplate) {
		this("", restTemplate);
	}
	
	public APIConnector(String baseUrl, RestOperations restOperations) {
		this(baseUrl, restOperations, defaultObjectMapper());
	}
	
	public APIConnector(RestOperations restOperations, ObjectMapper objectMapper) {
		this("", restOperations, objectMapper);
	}
	
	public APIConnector(String baseUrl, RestOperations restOperations, ObjectMapper objectMapper) {
		this.baseUrl = baseUrl;
		this.restOperations = restOperations;
		this.objectMapper = objectMapper;
	}

	// GET
	
	public <RES> RES get(String url, Object request, Class<RES> responseClass) throws RestClientException {
		return request(url, HttpMethod.GET, request, responseClass);
	}
	
	public <RES> RES get(String url, Object request, ParameterizedTypeReference<RES> responseType) throws RestClientException {
		return request(url, HttpMethod.GET, request, responseType);
	}
	
	// POST
	
	public <RES> RES post(String url, Object request, Class<RES> responseClass) throws RestClientException {
		return request(url, HttpMethod.POST, responseClass);
	}
	
	public <RES> RES post(String url, Object request, ParameterizedTypeReference<RES> responseType) throws RestClientException {
		return request(url, HttpMethod.POST, request, responseType);
	}
	
	// Request
	
	public <RES> RES request(String url, HttpMethod method, Class<RES> responseClass) throws RestClientException {
		return request(url, method, null, responseClass);
	}
	
	public <RES> RES request(String url, HttpMethod method, Object request, Class<RES> responseClass) throws RestClientException {
		return request(url, method, request, ParameterizedTypeReference.forType(responseClass));
	}
	
	public <RES> RES request(String url, HttpMethod method, ParameterizedTypeReference<RES> responseType) throws RestClientException {
		return request(url, method, null, responseType);
	}
	
	public <RES> RES request(String url, HttpMethod method, Object request, ParameterizedTypeReference<RES> responseType) throws RestClientException {
		 return request(url, method, null, request, responseType);
	}
	
	public <RES> RES requestMultipart(String url, MultiValueMap<String, Object> requestMap, Class<RES> responseClass) throws RestClientException {
		return requestMultipart(url, requestMap, ParameterizedTypeReference.forType(responseClass));
	}
	
	public <RES> RES requestMultipart(String url, MultiValueMap<String, Object> requestMap, ParameterizedTypeReference<RES> responseType) throws RestClientException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		return request(url, HttpMethod.POST, headers, requestMap, responseType);
	}
	
	public <REQ, RES> RES request(String url, HttpMethod method, HttpHeaders headers, REQ request, ParameterizedTypeReference<RES> responseType) throws RestClientException {
		Assert.notNull(url, "url must not be null");
		Assert.notNull(method, "method must not be null");
		Assert.notNull(responseType, "responseType must not be null");
		
		RequestEntity<REQ> requestEntity = new RequestEntity<REQ>(request, headers, method, URI.create(url));  
		ResponseEntity<RES> responeEntity = request(requestEntity, responseType);
		return responeEntity.getBody();
	}
	
	@SuppressWarnings( "unchecked" )
	public <REQ, RES> ResponseEntity<RES> request(RequestEntity<REQ> requestEntity, ParameterizedTypeReference<RES> responseType) throws RestClientException {
		Assert.notNull(requestEntity, "requestEntity must not be null");
		Assert.notNull(responseType, "responseType must not be null");
		
		HttpHeaders headers = requestEntity.getHeaders();
		if (headers == null || headers.isEmpty()) {
			headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
		}
		
		URI url = requestEntity.getUrl();
		if (!url.isAbsolute()) {
			url = URI.create(baseUrl + url.getPath());
		}
		
		REQ request = requestEntity.getBody();
		HttpMethod method = requestEntity.getMethod();
		
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> REQUEST TO EXTERNAL API >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.debug("Url: {} {}", method, url.toString());
		logger.debug("Headers: {}", serialize(headers));
		logger.debug("Request: {}", serialize(request));
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		
		REQ internalRequest = request;
		
		if (MediaType.MULTIPART_FORM_DATA.equals(headers.getContentType()) && MultiValueMap.class.isAssignableFrom(request.getClass())) {
			MultiValueMap<String, Object> internalMultiValueMap = new LinkedMultiValueMap<String, Object>();
			
			MultiValueMap<String, Object> requestMultiValueMap = (MultiValueMap<String, Object>) request;
			Set<String> reqParamNames = requestMultiValueMap.keySet();
			
			for (String paramName : reqParamNames) {
				LinkedList<Object> paramValueList = (LinkedList<Object>) requestMultiValueMap.get(paramName);
				
				for (Object paramValue : paramValueList) {
					Object interalParamValue = paramValue;
					if (paramValue != null) {
						Class<?> paramValueClass = paramValue.getClass();
						if (File.class.isAssignableFrom(paramValueClass)) {
							interalParamValue = new FileSystemResource((File) paramValue);
						} 
					}
					internalMultiValueMap.add(paramName, interalParamValue);
				}
			}
			
			internalRequest = (REQ) internalMultiValueMap;
		}
		
		long t1 = System.currentTimeMillis();
		
		ResponseEntity<RES> responseEntity = null;
		
		try {
			RequestEntity<REQ> internalRequestEntity = new RequestEntity<REQ>(internalRequest, headers, method, url);  
			responseEntity = restOperations.exchange(internalRequestEntity, responseType);
			
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< RESPONSE FROM EXTERNAL API <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			logger.debug("Status code: {}", responseEntity.getStatusCode());
			logger.debug("Headers: {}", serialize(responseEntity.getHeaders()));
			logger.debug("Response: {}", serialize (responseEntity.getBody()));
			logger.debug("Interval time: {}ms", System.currentTimeMillis() - t1);
			logger.debug("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			
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
					logger.error("Read timeout to external API.");
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
		
		return responseEntity;
	}
	
	protected String serialize(Object object) {
		String json = null;
		try {
			if (object != null) {
				json = objectMapper.writeValueAsString(object);
			}
		} catch ( JsonProcessingException e ) {
			logger.error("Error serialize: {}", object);
		}
		
		return json;
	}
	
	protected static ObjectMapper defaultObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper()
				.setNodeFactory(JsonNodeFactory.withExactBigDecimals(true))
				.setSerializationInclusion(Include.NON_NULL)
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		
		return objectMapper;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public RestOperations getRestOperations() {
		return restOperations;
	}
}