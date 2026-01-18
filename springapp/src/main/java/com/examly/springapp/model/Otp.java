package com.examly.springapp.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otpId;
    private String email;
    private String otpInput;
    private LocalDateTime validBy;

    public Otp(String email, String otpInput) {
        this.otpInput = otpInput;
        this.validBy = LocalDateTime.now().plusMinutes(10);
        this.email = email;
    }

    public Otp() {
    }

    public Long getOtpId() {
        return otpId;
    }

    public String getOtpInput() {
        return otpInput;
    }

    public void setOtpInput(String otpInput) {
        this.otpInput = otpInput;
    }

    public LocalDateTime getValidBy() {
        return validBy;
    }

    public void setValidBy(LocalDateTime validBy) {
        this.validBy = validBy;
    }

    public void setOtpId(Long otpId) {
        this.otpId = otpId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
