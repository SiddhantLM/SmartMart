package com.examly.springapp.controller;

import com.examly.springapp.service.OrderService;
import com.examly.springapp.service.RazorpayService;
import com.razorpay.Order;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

import java.util.Map;
import org.apache.commons.codec.digest.HmacUtils;

@RestController
@RequestMapping("/api/razorpay")
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/createOrder")
    public Map<String,Object> createOrder(@RequestBody Map<String,Object> body) throws Exception {
        // body should contain: amount (in rupees or paise depending on your UI). We'll expect rupees here.
        double amountRupees = Double.parseDouble(body.get("amount").toString());
        int amountPaise = (int) Math.round(amountRupees * 100);
        Order order = razorpayService.createOrder(amountPaise, "INR", "receipt_" + System.currentTimeMillis());
        JSONObject j = order.toJson();
        return Map.of(
            "id", j.getString("id"),
            "amount", j.getInt("amount"),
            "currency", j.getString("currency")
        );
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/verifyPayment")
    public Map<String, Object> verifyPayment(@RequestBody Map<String,String> payload, HttpServletRequest req) {
        // payload: razorpay_order_id, razorpay_payment_id, razorpay_signature
        String orderId = payload.get("razorpay_order_id");
        String paymentId = payload.get("razorpay_payment_id");
        String signature = payload.get("razorpay_signature");

        // Create the string order_id + '|' + payment_id, compute HMAC SHA256 with keySecret
        String payloadToVerify = orderId + "|" + paymentId;
        Dotenv dotenv = Dotenv.configure().load();
        String key = dotenv.get("RAZORPAY_KEY_SECRET");
        String expectedSignature = new HmacUtils("HmacSHA256", key).hmacHex(payloadToVerify);
        // (Alternatively inject key_secret from properties, but avoid logging it)

        boolean valid = expectedSignature.equals(signature);
        
        return Map.of("valid", valid);
    }
}