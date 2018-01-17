package com.example.springboot.dto.type;

import com.example.springboot.common.Type;

public enum UserType implements Type<String> {
	
	BASIC("01", "Basic user"),
	ADMIN("02", "Admin user");
	
	private final String value;
	private final String description;
	
	UserType(String value, String description) {
		this.value = value;
		this.description = description;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public String getDescription() {
		return description;
	}
}