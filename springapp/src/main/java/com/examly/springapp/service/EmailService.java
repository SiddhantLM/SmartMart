
package com.examly.springapp.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpStatusCodeException;

@Service
public class EmailService {

    private static final String BREVO_URL = "https://api.brevo.com/v3/smtp/email";

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${brevo.api.key}")
    private String apiKey;

    @Value("${brevo.sender.email}")
    private String senderEmail;

    @Value("${brevo.sender.name}")
    private String senderName;

    private void send(String to, String subject, String body)
            throws IOException {

        HttpHeaders headers = new HttpHeaders();
        headers.set("api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        Map<String, Object> sender = Map.of(
                "name", senderName,
                "email", senderEmail);

        Map<String, Object> recipient = Map.of(
                "email", to);

        Map<String, Object> requestBody = Map.of(
                "sender", sender,
                "to", List.of(recipient),
                "subject", subject,
                "textContent", body);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    BREVO_URL, request, String.class);

            System.out.println("Brevo Status: " + response.getStatusCode());
            System.out.println("Brevo Response: " + response.getBody());

        } catch (HttpStatusCodeException e) {
            System.err.println("Brevo Error: " + e.getStatusCode());
            System.err.println("Brevo Response: " + e.getResponseBodyAsString());

            throw new IOException("Brevo email failed: "
                    + e.getResponseBodyAsString(), e);
        }
    }

    public void sendOtpEmail(String toEmail, String otp)
            throws IOException {
        send(
                toEmail,
                "Your OTP Code",
                "Your OTP is: " + otp + "\n\nValid for 10 minutes.");
    }

    public void sendStockReminderEmail(String toEmail, String productName)
            throws IOException {
        send(
                toEmail,
                "Stock Reminder",
                "The product '" + productName + "' is back in stock!");
    }

    public void sendContactInfo(String info) throws IOException {
        send(
                "sathyasailikhith@gmail.com",
                "Contact Request",
                info);
    }
}
