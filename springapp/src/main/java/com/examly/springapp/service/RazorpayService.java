package com.examly.springapp.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {
    private final RazorpayClient client;

    public RazorpayService(@Value("${razorpay.key_id}") String key, @Value("${razorpay.key_secret}") String secret) throws Exception {
        this.client = new RazorpayClient(key, secret);
    }

    public Order createOrder(int amountInPaise, String currency, String receiptId) throws Exception {
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amountInPaise);      // amount in paise
        orderRequest.put("currency", currency);         // "INR"
        orderRequest.put("receipt", receiptId);
        orderRequest.put("payment_capture", 1);         // auto-capture
        return client.orders.create(orderRequest);
    }
}
