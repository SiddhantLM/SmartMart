package com.examly.springapp.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.examly.springapp.model.Otp;
import com.examly.springapp.model.RegisterRequestDTO;
import com.examly.springapp.model.User;

public interface UserService {
    User createUser (RegisterRequestDTO user);
    UserDetails loadUserByUsername(String userName);
    List<User> findAllUsers ();
    User loginUser (User user);
    User getById(Long userId);
    User deleteUserById (Long userId);
    void sendOtp (String email);
    // boolean resendOtp (String email);
}

