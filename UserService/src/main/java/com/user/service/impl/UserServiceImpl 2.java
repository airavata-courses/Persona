package com.user.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.entity.User;
import com.user.repo.UserRepo;
import com.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepo userRepo;

	@Override
	public String saveUser(String userName) {
		Optional<User> optUser = userRepo.findByUserName(userName);
		if (!optUser.isPresent()) {
			User user = new User();
			user.setUserName(userName);
			userRepo.save(user);
			return "User registered successfully";
		}
		return "User is already registered";
	}

}
