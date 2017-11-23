package com.example.springboot.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors (fluent = true)
public class UserDto {

	private Integer id;
	private String username;
	private String password;
	private String lastName;
	private String firstName;
	private String roleID;
}
