package com.examly.springapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.model.StockReminder;
import com.examly.springapp.service.StockReminderService;

@RestController
@RequestMapping("/api/stock-reminder")
public class StockReminderController {
    @Autowired
    StockReminderService service;

    @PostMapping("/product/{productId}")
    public ResponseEntity<Boolean> addReminder (@PathVariable Long productId, @RequestBody Map<String,String> payload) {
        try {
            service.addReminder(productId, payload.get("email"));
            return new ResponseEntity<>(true, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>( false, HttpStatusCode.valueOf(500));
        }
    }
    
    @PostMapping("/{reminderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boolean> sendReminder (@PathVariable Long reminderId) {
        try {
            service.sendReminder(reminderId);
            return new ResponseEntity<>(true, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>( false, HttpStatusCode.valueOf(500));
        }
    }

    @GetMapping()
    public ResponseEntity<List<StockReminder>> getReminder(){
        try {
            List<StockReminder> stock=service.getReminder();
            return new ResponseEntity<>(stock,HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }
}
