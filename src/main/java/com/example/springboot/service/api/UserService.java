package com.example.springboot.service.api;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.example.springboot.dto.UserDto;

public interface UserService {

	void doSth();
	
	UserDto selectUser(Integer id);
	
	@Transactional
	int insertUserBatch(List<UserDto> userList);
}
