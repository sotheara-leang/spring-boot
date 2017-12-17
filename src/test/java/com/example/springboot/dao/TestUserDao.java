package com.example.springboot.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.example.springboot.AbstractTestCase;
import com.example.springboot.dto.UserDto;
import com.example.springboot.dto.UserFilterDto;

public class TestUserDao extends AbstractTestCase {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PlatformTransactionManager txManager;
	
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
	
	@Test
	public void testInsertBatchWithTransactional() {
		int size = 10;
		
		List<UserDto> userList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			UserDto userDto = UserDto.builder()
					.username("user_" + (i > 5 ? "ssssssssssssssssssss" : (i+1)))
					.password("123")
					.lastName("leang_" + (i+1))
					.firstName("sotheara_" + (i+1))
					.roleID("2")
					.build();
			
			userList.add(userDto);
		}
		
		TransactionStatus status = txManager.getTransaction(new DefaultTransactionDefinition());
		try {
			userDao.insertUserBatch(userList);
			txManager.commit(status);
			
		} catch (Exception e) {
			txManager.rollback(status);
			e.printStackTrace();
		}
	}
}
