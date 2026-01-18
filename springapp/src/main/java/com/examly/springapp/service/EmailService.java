package com.examly.springapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Async
    public void sendOtpEmail(String toEmail, String otp) {
        sendMail(
                toEmail,
                "Your OTP Code",
                "Your OTP is: " + otp + "\nIt is valid for 10 minutes.");
    }

    public void sendStockReminderEmail(String toEmail, String productName) {
        sendMail(
                toEmail,
                "Stock Reminder",
                "Hello,\n\nThe product '" + productName + "' is back in stock!");
    }

    public void sendContactInfo(String info) {
        sendMail(
                "sathyasailikhith@gmail.com",
                "Contact",
                info);
    }

    private void sendMail(String to, String subject, String content) {
        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content mailContent = new Content("text/plain", content);
        Mail mail = new Mail(from, subject, toEmail, mailContent);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
        } catch (Exception e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
