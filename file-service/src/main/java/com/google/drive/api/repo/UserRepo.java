package com.google.drive.api.repo;

import com.google.drive.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

   Optional<User> findByUserName(String username);
}
