package com.user.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.user.entity.User;

@Repository
public interface UserRepo  extends JpaRepository<User, Long>{
	
	Optional<User> findByUserName(String userName);

}
