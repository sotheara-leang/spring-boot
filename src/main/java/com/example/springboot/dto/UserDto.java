package com.example.springboot.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {

	private static final long serialVersionUID = 8837977785640480792L;
	
	private Integer id;
	private String username;
	private String password;
	private String lastName;
	private String firstName;
	private String roleID;
}
