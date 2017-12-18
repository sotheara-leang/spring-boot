package com.example.springboot.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.example.springboot.AbstractServiceTest;
import com.example.springboot.dto.UserDto;
import com.example.springboot.dto.UserFilterDto;

public class UserDaoTest extends AbstractServiceTest {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PlatformTransactionManager txManager;
	
	@Test
	public void testSelect() {
		UserDto userDto = userDao.selectUser(1);
		assertThat(userDto).isNotNull();
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
		
		int result = userDao.insertUser(userDto);
		
		assertThat(result).isEqualTo(1);
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
		
		int result = userDao.insertUserBatch(userList);
		
		assertThat(result).isEqualTo(10);
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
