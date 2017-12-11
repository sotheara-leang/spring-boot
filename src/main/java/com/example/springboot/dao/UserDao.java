package com.example.springboot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.springboot.dto.UserDto;

@Repository
public interface UserDao {

	UserDto selectUser(Integer id);
	
	List<UserDto> selectByLastNameAndFirstName(@Param("lastName") String lastName, @Param("firstName") String firstName);
	
	int insertUser(UserDto user);
	
}
