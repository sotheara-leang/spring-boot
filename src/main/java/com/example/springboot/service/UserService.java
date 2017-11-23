package com.example.springboot.service;

import com.example.springboot.dto.UserDto;

public interface UserService {

	UserDto selectUser(Integer id);
}
