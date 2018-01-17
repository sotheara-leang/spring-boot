package com.example.springboot.common;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springboot.AbstractServiceTest;
import com.example.springboot.dto.MyDto;
import com.example.springboot.dto.type.UserType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EnumTypeTest extends AbstractServiceTest {
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	public void doSerializer() throws Exception {
		MyDto myDto = new MyDto();
		myDto.setUserType( UserType.BASIC );
		
		String json = objectMapper.writeValueAsString( myDto );
		System.out.println( json );
	}
	
	@Test
	public void doDeserializer() throws Exception {
		MyDto object = objectMapper.readValue( "{\"userType\":\"01\"}", MyDto.class );
		System.out.println( object );
	}
}
