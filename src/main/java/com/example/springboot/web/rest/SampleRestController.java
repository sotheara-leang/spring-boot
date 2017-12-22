package com.example.springboot.web.rest;

import javax.websocket.server.PathParam;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springboot.dto.UserDto;

@RestController
public class SampleRestController {

	@GetMapping("/api/user/{id}")
	public UserDto getUser(@PathParam("id") String id) {
		return UserDto.builder()
				.username("user" + id)
				.lastName("sok_"+ id)
				.firstName("dara_" + id)
				.build();
	}
	
	@PostMapping("/api/upload")
	public String uploadFile(MultipartFile file) {
		return "Success";
	}
	
	@PostMapping("/api/upload/list")
	public String uploadFileList(MultipartFile[] files) {
		return "Success";
	}
}
