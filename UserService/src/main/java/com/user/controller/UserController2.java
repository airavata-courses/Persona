package com.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.request.UserRequest;
import com.user.service.UserService;

@RestController
@RequestMapping("user")
public class UserController2 {

	@Autowired
	private UserService userService;

	@GetMapping
	public String get() {
		return "SUCCESS";

	}

	@PostMapping("save")
	public ResponseEntity<String> saveUser(@RequestBody UserRequest request) {

		return new ResponseEntity<String>(userService.saveUser(request
				.getUserName()), HttpStatus.OK);
	}
}
