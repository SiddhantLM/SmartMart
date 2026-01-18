package com.examly.springapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP is: " + otp + "\nIt is valid for 10 minutes.");
        mailSender.send(message);
    }

    public void sendStockReminderEmail(String toEmail, String productName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Stock Reminder");
        message.setText("Hello,\n\nThe product '" + productName + "' is back in stock!\n\nRegards,\nSmart Mart");
        mailSender.send(message);
    }

    public void sendContactInfo(String info) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("sathyasailikhith@gmail.com");
        message.setSubject("Contact");
        message.setText(info);
        mailSender.send(message);
    }

}
