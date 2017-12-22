package com.example.springboot.common;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.springboot.AbstractWebTest;
import com.example.springboot.common.connector.APIConnector;
import com.example.springboot.dto.UserDto;

public class APIConnectorTest extends AbstractWebTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	private APIConnector connector; 

	
	@Before
	public void init() {
		RestTemplate restTemplate = testRestTemplate.getRestTemplate();

		URI url = URI.create(((RootUriTemplateHandler) restTemplate.getUriTemplateHandler()).getRootUri());
		
		connector = new APIConnector(url.toString(), testRestTemplate.getRestTemplate());
	}
	
	@Test
	public void testGetUser() throws Exception {
		connector.get("/api/user/1", null, UserDto.class);
	} 
	
	@Test
	public void testUploadFile() throws Exception {
		ClassPathResource resource = new ClassPathResource("application-test.yml");
		
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", resource.getFile());
		
		connector.requestMultipart("/api/upload", map, String.class);
	} 
}