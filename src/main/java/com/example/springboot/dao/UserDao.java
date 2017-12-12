package com.example.springboot.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.example.springboot.dto.PageDto;
import com.example.springboot.dto.UserDto;
import com.example.springboot.dto.UserFilterDto;

@Repository
public interface UserDao {

	UserDto selectUser(Integer id);
	
	List<UserDto> selectByLastNameAndFirstName(@Param("lastName") String lastName, @Param("firstName") String firstName);
	
	List<UserDto> selectByPage(@Param("filter") UserFilterDto filter, @Param("page") PageDto page);
	
	int insertUser(UserDto user);
	
	int insertUserBatch(@Param("userList") List<UserDto> userList);
}
