package com.example.springboot.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.springboot.AbstractTest;
import com.example.springboot.dto.UserDto;
import com.example.springboot.dto.UserFilterDto;

public class TestUserDao extends AbstractTest {

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
		UserDto userDto = UserDto.builder()
				.username("user111")
				.password("123")
				.lastName("leang")
				.firstName("sotheara")
				.roleID("2")
				.build();
		
		UserDto userDto2 = UserDto.builder()
				.username("user222")
				.password("222")
				.lastName("sok")
				.firstName("dara")
				.roleID("1")
				.build();
		
		List<UserDto> userList = new ArrayList<>();
		userList.add(userDto);
		userList.add(userDto2);
		
		userDao.insertUserBatch(userList);
	}
}
