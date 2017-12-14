package com.example.springboot.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springboot.AbstractTestCase;
import com.example.springboot.dto.UserDto;
import com.example.springboot.dto.UserFilterDto;

public class TestUserDao extends AbstractTestCase {

	@Autowired
	private UserDao userDao;
	
	@Test
	public void testSelect() {
		UserDto selectUser = userDao.selectUser(1);
		Assert.assertNotNull(selectUser);
	}
	
	@Test
	public void testSelectByLastNameAndFirstName() {
		userDao.selectByLastNameAndFirstName("sss", "bbb");
	}
	
	@Test
	public void testSelectByPage() {
		UserFilterDto filter = new UserFilterDto();
		filter.setFirstName("sotheara");
		filter.setLastName("leang");
		
		userDao.selectByPage(filter, null);
	}
	
	@Test
	public void testInsert() {
		UserDto userDto = UserDto.builder()
				.username("username")
				.password("123")
				.lastName("leang")
				.firstName("sotheara")
				.roleID("2")
				.build();
		
		userDao.insertUser(userDto);
	}
	
	@Test
	public void testInsertBatch() {
		int size = 10;
		
		List<UserDto> userList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			UserDto userDto = UserDto.builder()
					.username("user_" + (i+1))
					.password("123")
					.lastName("leang_" + (i+1))
					.firstName("sotheara_" + (i+1))
					.roleID("2")
					.build();
			
			userList.add(userDto);
		}
		
		userDao.insertUserBatch(userList);
	}
}
