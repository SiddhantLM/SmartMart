package com.examly.springapp.model;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class LoginDTO {

    private String token;
    private String userName;
    @Enumerated (EnumType.STRING)
    private UserRole userRole;
    private Long userId;
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public UserRole getUserRole() {
        return userRole;
    }
    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public LoginDTO() {
    }
    public LoginDTO(String token, String userName, UserRole userRole, Long userId) {
        this.token = token;
        this.userName = userName;
        this.userRole = userRole;
        this.userId = userId;
    }
    
}
