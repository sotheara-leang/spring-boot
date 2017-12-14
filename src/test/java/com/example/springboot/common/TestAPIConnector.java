package com.example.springboot.common;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.example.springboot.AbstractTestCase;
import com.example.springboot.common.connector.APIConnector;
import com.example.springboot.dto.UserDto;

public class TestAPIConnector extends AbstractTestCase {
	
	private APIConnector connector = new APIConnector("http://localhost:9090", new RestTemplate());
	
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
