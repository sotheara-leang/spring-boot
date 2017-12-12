package com.example.springboot.dto;

public class UserFilterDto {

	private String lastName;
	private String firstName;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Override
	public String toString() {
		return "UserFilterDto [lastName=" + lastName + ", firstName=" + firstName + "]";
	}
}
