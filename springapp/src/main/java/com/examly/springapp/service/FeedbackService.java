package com.examly.springapp.service;

import java.util.List;

import com.examly.springapp.model.Feedback;
import com.examly.springapp.model.User;

public interface FeedbackService {

public Feedback creatFeedback(Long userId, Feedback feedback);
public Feedback getFeedBackById(Long feedbackId);
public List<Feedback> getAllFeedback();
public Feedback deleteFeedback(long feedbackId);
public List<Feedback> getFeedbackByUserId(long userId);
public User getUserById(Long userId);


    
}
