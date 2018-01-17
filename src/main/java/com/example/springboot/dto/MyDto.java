package com.example.springboot.dto;

import com.example.springboot.dto.type.UserType;

public class MyDto {
	
	private UserType userType;

	public UserType getUserType() {
		return userType;
	}

	public void setUserType( UserType userType ) {
		this.userType = userType;
	}

	@Override
	public String toString() {
		return "MyDto [userType=" + userType + "]";
	}
}
