package com.example.springboot.dao;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springboot.AbstractTest;
import com.example.springboot.dto.UserDto;

public class TestUserDao extends AbstractTest {

	@Autowired
	private UserDao userDao;
	
	@Test
	public void doSelect() {
		UserDto selectUser = userDao.selectUser(1);
		Assert.assertNotNull(selectUser);
	}
}
