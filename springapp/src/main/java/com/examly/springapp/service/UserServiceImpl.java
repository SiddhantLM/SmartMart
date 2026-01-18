package com.examly.springapp.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.examly.springapp.config.UserPrinciple;
import com.examly.springapp.exception.DuplicateUsernameException;
import com.examly.springapp.model.Cart;
import com.examly.springapp.model.Otp;
import com.examly.springapp.model.RegisterRequestDTO;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.OtpRepo;
import com.examly.springapp.repository.UserRepo;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo repo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    OtpRepo otpRepo;

    @Autowired
    EmailService emailService;

    @Override
    public User createUser(RegisterRequestDTO user) {
        User existingUser = repo.findByUsername(user.getUser().getUsername());
        if (existingUser != null) {
            throw new DuplicateUsernameException("Username already exists");
        }
        // user.setUserId(null);

        Otp otp = otpRepo.findByEmail(user.getUser().getEmail().trim().toLowerCase());

        if (otp == null) {
            throw new RuntimeException("OTP record not found");
        }

        if (!otp.getOtpInput().equals(user.getOtp()) || otp.getValidBy().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP incorrect");
        }
        otpRepo.delete(otp);
        user.getUser().setPassword(passwordEncoder.encode(user.getUser().getPassword()));
        Cart cart = new Cart();
        cart.setQuantity(0);
        cart.setTotalAmount(0.0);
        cart.setItems(new ArrayList<>());
        cart.setCreatedAt(LocalDate.now());
        cart.setUpdatedAt(LocalDate.now());

        // Link both sides of bidirectional relationship
        user.getUser().setCart(cart);
        cart.setUser(user.getUser()); // THIS LINE WAS MISSING - NOW FIXED!

        return repo.save(user.getUser());
    }

    @Override
    public User loginUser(User user) {
        User existingUser = repo.findByUsername(user.getUsername());

        if (existingUser != null) {
            if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                return existingUser;
            } else {
                return null;
            }
        }

        return null;

    }

    @Override
    public List<User> findAllUsers() {
        return repo.findAll();
    }

    @Override
    public User deleteUserById(Long userId) {
        User existingUser = repo.findById(userId).orElse(null);

        if (existingUser != null) {
            repo.deleteById(userId);
            return existingUser;
        }

        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        User eistingUser = repo.findByUsername(userName);

        if (eistingUser != null) {
            return new UserPrinciple(eistingUser);
        }

        return null;
    }

    @Override
    public User getById(Long userId) {
        return repo.findById(userId).orElse(null);
    }

    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // ensures 6 digits
        return String.valueOf(otp);
    }

    @Override
    public void sendOtp(String email) {
        String trimmedEmail = email.trim().toLowerCase();

        Otp existingOtp = otpRepo.findByEmail(trimmedEmail);
        String otpContent = generateOtp();

        if (existingOtp != null) {
            existingOtp.setOtpInput(otpContent);
            existingOtp.setValidBy(LocalDateTime.now().plusMinutes(10));
        } else {
            existingOtp = new Otp(trimmedEmail, otpContent);
            existingOtp.setValidBy(LocalDateTime.now().plusMinutes(10));
        }

        otpRepo.save(existingOtp);
        emailService.sendOtpEmail(trimmedEmail, otpContent);

        // For testing (remove in production)
        System.out.println("OTP for " + trimmedEmail + " : " + otpContent);
    }
}