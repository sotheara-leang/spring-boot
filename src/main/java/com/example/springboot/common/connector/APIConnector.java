package com.example.springboot.common.connector;

import java.io.File;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

public class APIConnector {
	
	private static final Logger logger = LoggerFactory.getLogger(APIConnector.class);
	
	private static final ObjectMapper objectMapper = new ObjectMapper()
			.setNodeFactory( JsonNodeFactory.withExactBigDecimals(true) )
			.setSerializationInclusion( Include.NON_NULL )
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
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
	
	public <REQ, RES> RES get(String url, REQ request, Class<RES> responseClass) throws RestClientException, URISyntaxException {
		return request(url, HttpMethod.GET, request, responseClass);
	}
	
	public <REQ, RES> RES get(String url, REQ request, ParameterizedTypeReference<RES> responseType) throws URISyntaxException, RestClientException {
		return request(url, HttpMethod.GET, request, responseType);
	}
	
	// POST
	
	public <REQ, RES> RES post(String url, REQ request, Class<RES> responseClass) throws RestClientException, URISyntaxException {
		return request(url, HttpMethod.POST, responseClass);
	}
	
	public <REQ, RES> RES post(String url, REQ request, ParameterizedTypeReference<RES> responseType) throws URISyntaxException, RestClientException {
		return request(url, HttpMethod.POST, request, responseType);
	}
	
	// Request
	
	public <REQ, RES> RES request(String url, HttpMethod method, Class<RES> responseClass) throws RestClientException, URISyntaxException {
		return request(url, method, null, responseClass);
	}
	
	public <REQ, RES> RES request(String url, HttpMethod method, REQ request, Class<RES> responseClass) throws RestClientException, URISyntaxException {
		return request(url, method, request, new ParameterizedTypeReference<RES>() {

			@Override
			public Type getType() {
				return responseClass;
			}
		});
	}
	
	public <REQ, RES> RES request(String url, HttpMethod method, ParameterizedTypeReference<RES> responseType) throws RestClientException, URISyntaxException {
		return request(url, method, null, responseType);
	}
	
	public <REQ, RES> RES request(String url, HttpMethod method, REQ request, ParameterizedTypeReference<RES> responseType) throws URISyntaxException, RestClientException {
		 return request(url, method, null, request, responseType);
	}
	
	public <RES> RES requestMultipart(String url, HttpMethod method, Map<String, Object> requestMap, ParameterizedTypeReference<RES> responseType) throws URISyntaxException, RestClientException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		
		return request(url, method, headers, requestMap, responseType);
	}
	
	@SuppressWarnings("unchecked")
	public <REQ, RES> RES request(String url, HttpMethod method, HttpHeaders headers, REQ request, ParameterizedTypeReference<RES> responseType) throws URISyntaxException, RestClientException {
		RES response = null;
		
		String fullUrl = baseUrl + url;
		long t1 = System.currentTimeMillis();
		
		try {
			logger.debug("========== Request to external API ==========");
			logger.debug("Url: " + fullUrl);
			logger.debug("Request: " + serialize( request ));
			
			MediaType contentType = headers.getContentType();
			if (MediaType.MULTIPART_FORM_DATA == contentType && Map.class.isAssignableFrom(request.getClass())) {
				Map<String, Object> requestMap = (Map<String, Object>) request;
				
				Set<String> reqParamNames = requestMap.keySet();
				for (String paramName : reqParamNames) {
					Object paramValue = requestMap.get(paramName);
					
					if (File.class.isAssignableFrom(paramValue.getClass())) {
						FileSystemResource fileResouce = new FileSystemResource((File) paramValue);
						requestMap.put(paramName, fileResouce);
					}
				}
			}
			
			RequestEntity<REQ> reqEntity = new RequestEntity<REQ>(request, headers, method, new URI(fullUrl));  
			ResponseEntity<RES> resEntity = restOperations.exchange(reqEntity, responseType);
			response = resEntity.getBody();
			
			logger.debug("========== Response from external API ==========");
			logger.debug("Interval time : {}ms", System.currentTimeMillis() - t1);
			logger.debug("Response: " + serialize ( response ));
			
		} catch (URISyntaxException e) {
			logger.error(">>> Url is incorrect: {}", fullUrl, e);
			throw e;
			
		} catch (RestClientException e) {
			Throwable cause = e.getCause();
			
			if (cause instanceof ConnectException) {
				logger.error(">>> Connection refused to external API.");
			} else if (cause instanceof ConnectionPoolTimeoutException) {
				logger.error(">>> Connection pool timeout.");
			} else if (cause instanceof ConnectTimeoutException) {
				logger.error(">>> Connection timeout to external API.");
			} else if (cause instanceof SocketTimeoutException) {
				logger.error(">>> Read time out to external API.");
			} else if (cause instanceof SocketException) {
				logger.error(">>> Error creating/accessing connection.");
			} else {
				logger.error(">>> Error while calling external API.");
			}
			
			logger.error("Interval time: {}ms", System.currentTimeMillis() - t1, e);
			throw e;
			
		} catch (RuntimeException e) {
			logger.error(">>> Error while calling external API");
			logger.error("Interval time: {}ms", System.currentTimeMillis() - t1, e);
			throw e;
			
		} finally {
			logger.debug("========== End calling external API ==========");
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