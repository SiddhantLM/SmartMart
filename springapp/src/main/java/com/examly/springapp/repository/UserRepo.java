package com.examly.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.examly.springapp.model.User;

public interface UserRepo extends JpaRepository<User,Long> {
    @Query("Select u from User u where u.username = :username")
    public User findByUsername (String username);
}
