package com.examly.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.model.Otp;

public interface OtpRepo extends JpaRepository<Otp,Long> {
    Otp findByEmail (String email);
}
