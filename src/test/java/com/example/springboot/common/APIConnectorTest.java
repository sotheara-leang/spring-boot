package com.example.springboot.common;

import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RootUriTemplateHandler;
import org.springframework.core.io.ByteArrayResource;
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
		ClassPathResource resource = new ClassPathResource("application-test-service.yml");
		
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("file", resource.getFile());
		
		connector.requestMultipart("/api/upload", map, String.class);
	} 
	
	@Test
	public void testUploadFileList() throws Exception {
		ClassPathResource resourceResource = new ClassPathResource("application-test-service.yml");
		ClassPathResource resourceResource2 = new ClassPathResource("application-test-web.yml");
		
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("files", resourceResource.getFile());
		map.add("files", resourceResource2.getFile());
		
		connector.requestMultipart("/api/upload/list", map, String.class);
	}
	
	@Test
	public void testDownload() throws Exception {
		ByteArrayResource inputStreamResource = connector.get("/resources/styles/main.css", null, ByteArrayResource.class);
		
		String response = IOUtils.toString(inputStreamResource.getInputStream());
		
		System.out.println(response);
	}
}
