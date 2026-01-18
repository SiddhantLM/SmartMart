package com.examly.springapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.config.JwtUtils;
import com.examly.springapp.exception.DuplicateUsernameException;
import com.examly.springapp.model.LoginDTO;
import com.examly.springapp.model.RegisterRequestDTO;
import com.examly.springapp.model.User;
import com.examly.springapp.service.EmailService;
import com.examly.springapp.service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService service;

    @Autowired
    EmailService emailService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/api/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO user) {
        try {
            System.out.println("here");
            User registeredUser = service.createUser(user);
            return new ResponseEntity<>(registeredUser, HttpStatusCode.valueOf(201));
        } catch (DuplicateUsernameException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(409));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(401));
        }
    }

    @PostMapping("/api/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            User loggedInUser = service.loginUser(user);

            if (loggedInUser != null) {
                LoginDTO loginData = new LoginDTO(jwtUtils.generateToken(loggedInUser.getUsername()),
                        loggedInUser.getUsername(), loggedInUser.getUserRole(), loggedInUser.getUserId());
                return new ResponseEntity<>(loginData, HttpStatusCode.valueOf(200));
            } else {
                return new ResponseEntity<>("Invalid Credentials!", HttpStatusCode.valueOf(402));
            }

        } catch (Exception e) {

            return new ResponseEntity<>("Error while logging in!", HttpStatusCode.valueOf(500));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/user")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = service.findAllUsers();
            return new ResponseEntity<>(users, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/api/user/{userId}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long userId) {
        try {
            User deletedUser = service.deleteUserById(userId);

            if (deletedUser != null) {
                return new ResponseEntity<>(true, HttpStatusCode.valueOf(200));
            } else {
                return new ResponseEntity<>(false, HttpStatusCode.valueOf(404));
            }

        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatusCode.valueOf(403));
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/api/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        try {
            User user = service.getById(userId);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
            } else {
                return new ResponseEntity<>(HttpStatusCode.valueOf(500));
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    @PostMapping("/api/user/send-otp")
    public ResponseEntity<Boolean> sendOtp(@RequestBody Map<String, String> payload) {
        System.out.println("here");
        try {
            String email = payload.get("email");
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(false);
            }
            service.sendOtp(email.trim().toLowerCase());
            return new ResponseEntity<>(true, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(false);
        }
    }

    @PostMapping("/api/user/send-contact-info")
    public ResponseEntity<Boolean> sendContactInfo(@RequestBody Map<String, String> payload) {
        try {
            String info = payload.get("info");
            emailService.sendContactInfo(info);
            return new ResponseEntity<>(true, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(false);
        }
    }

}
