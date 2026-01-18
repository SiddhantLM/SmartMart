package com.examly.springapp.model;

public class RegisterRequestDTO {
    private User user;
    private String otp;
    public RegisterRequestDTO(User user, String otp) {
        this.user = user;
        this.otp = otp;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public String getOtp() {
        return otp;
    }
    public void setOtp(String otp) {
        this.otp = otp;
    }
    public RegisterRequestDTO() {
    }

}
