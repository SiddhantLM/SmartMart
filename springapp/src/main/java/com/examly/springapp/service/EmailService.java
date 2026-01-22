package com.examly.springapp.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailService {

    private static final String EMAIL_ENDPOINT = "mail/send";

    private final SendGrid sendGrid;
    private final Email fromEmail;

    public EmailService(SendGrid sendGrid, Email fromEmail) {
        this.sendGrid = sendGrid;
        this.fromEmail = fromEmail;
    }

    private void send(String to, String subject, String body) throws IOException {
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint(EMAIL_ENDPOINT);
        request.setBody(mail.build());

        sendGrid.api(request);
    }

    // 🔐 OTP EMAIL (FIXED)
    public void sendOtpEmail(String toEmail, String otp) throws IOException {
        send(
                toEmail,
                "Your OTP Code",
                "Your OTP is: " + otp + "\n\nValid for 10 minutes.");
    }

    // 📦 STOCK REMINDER
    public void sendStockReminderEmail(String toEmail, String productName) throws IOException {
        send(
                toEmail,
                "Stock Reminder",
                "The product '" + productName + "' is back in stock!");
    }

    // 📩 CONTACT EMAIL
    public void sendContactInfo(String info) throws IOException {
        send(
                "sathyasailikhith@gmail.com",
                "Contact Request",
                info);
    }
}
