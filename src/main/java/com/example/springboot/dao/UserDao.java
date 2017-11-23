package com.example.springboot.dao;

import org.springframework.stereotype.Repository;

import com.example.springboot.dto.UserDto;

@Repository
public interface UserDao {

	UserDto selectUser(Integer id);
}
