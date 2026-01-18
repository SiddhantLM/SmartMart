package com.examly.springapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Feedback;
import com.examly.springapp.model.User;
import com.examly.springapp.repository.FeedbackRepo;
import com.examly.springapp.repository.UserRepo;

@Service
public class FeedbackServiceImpl implements FeedbackService{

    @Autowired
    FeedbackRepo frepo;

    @Autowired
    UserRepo urepo;

@Override
public Feedback creatFeedback(Long userId, Feedback feedback) {
    User user = urepo.findById(userId).orElse(null);

    if(user!=null) {
        feedback.setUser(user);
        return frepo.save(feedback);
    }

    return null;
}

@Override
public Feedback getFeedBackById(Long feedbackId) {
   return frepo.findById(feedbackId).orElse(null);
}

@Override
public List<Feedback> getAllFeedback() {
  return frepo.findAll();
}

@Override
public Feedback deleteFeedback(long feedbackId) {
    Feedback feed= frepo.findById(feedbackId).orElse(null);
    if(feed != null)
    {
       frepo.deleteById(feedbackId);
       return feed;
    }
    return null;

   
}

@Override
public List<Feedback> getFeedbackByUserId(long userId) {
    return frepo.findByUserId(userId);
}

@Override
public User getUserById(Long userId) {
    return urepo.findById(userId).orElse(null);
}

}
