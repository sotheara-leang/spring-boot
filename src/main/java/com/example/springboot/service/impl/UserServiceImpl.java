package com.example.springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springboot.dao.UserDao;
import com.example.springboot.dto.UserDto;
import com.example.springboot.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	
	@Override
	public UserDto selectUser(Integer id) {
		return userDao.selectUser(id);
	}
}
