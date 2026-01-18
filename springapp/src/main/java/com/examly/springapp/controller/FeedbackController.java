package com.examly.springapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.model.Feedback;
import com.examly.springapp.model.User;
import com.examly.springapp.service.FeedbackService;

    @RestController
    @RequestMapping("/api/feedback")
    public class FeedbackController {
    
        @Autowired
        private FeedbackService feedbackService;
    
       
        @PreAuthorize("hasRole('USER')")
        @PostMapping("/user/{userId}")
        public ResponseEntity<?> createFeedback(@PathVariable Long userId, @RequestBody Feedback feedback ) {
            try {
                Feedback savedFeedback = feedbackService.creatFeedback(userId, feedback);
                return new ResponseEntity<>(savedFeedback, HttpStatusCode.valueOf(201));
            } catch (Exception e) {
                return new ResponseEntity<>( HttpStatusCode.valueOf(500));
            } 
        }

        @GetMapping
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<?> getAllFeedback() {
            List<Feedback> feedbackList = feedbackService.getAllFeedback();
                return new ResponseEntity<>(feedbackList, HttpStatusCode.valueOf(200)); 
        }
    
        
        @GetMapping("/user/{userId}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<?> getFeedbackByUserId(@PathVariable long userId) {
            List<Feedback> feedback = feedbackService.getFeedbackByUserId(userId);
            if (feedback != null) {
                return new ResponseEntity<>(feedback, HttpStatusCode.valueOf(200)); 
            } else {
                return new ResponseEntity<>("Feedback not found", HttpStatusCode.valueOf(404)); 
            }
        }
    
        @DeleteMapping("/{feedbackId}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<?> deleteFeedback(@PathVariable long feedbackId) {
            Feedback deleted = feedbackService.deleteFeedback(feedbackId);
            if (deleted!=null) {
                return new ResponseEntity<>(deleted, HttpStatusCode.valueOf(200)); 
            } else {
                return new ResponseEntity<>(HttpStatusCode.valueOf(404)); 
            }
        }

        
        @GetMapping("/{feedbackId}")
        @PreAuthorize("hasRole('USER')")
        public ResponseEntity<?> getFeedbackById(@PathVariable long feedbackId) {
            try {
                Feedback feedback = feedbackService.getFeedBackById(feedbackId);
                if (feedback != null) {
                    return new ResponseEntity<>(feedback, HttpStatusCode.valueOf(200)); 
                } else {
                    return new ResponseEntity<>("Feedback not found", HttpStatusCode.valueOf(404)); 
                }
            } catch (Exception e) {
                return new ResponseEntity<>("Access Denied or Error occurred", HttpStatusCode.valueOf(403)); 
            }

        }

        @GetMapping("/getUserById/{userId}")
        public ResponseEntity<User> getUserById(@PathVariable long userId){
            return new ResponseEntity<>(feedbackService.getUserById(userId), HttpStatusCode.valueOf(200));
        }
}

   
