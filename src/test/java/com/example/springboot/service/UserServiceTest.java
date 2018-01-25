package com.example.springboot.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springboot.AbstractServiceTest;
import com.example.springboot.dto.UserDto;

public class UserServiceTest extends AbstractServiceTest {

	@Autowired
	private UserService userService;
	
	@Test
	public void doTest() {
		UserDto selectUser = userService.selectUser( 1 );
		System.out.println( selectUser );
	}
}
