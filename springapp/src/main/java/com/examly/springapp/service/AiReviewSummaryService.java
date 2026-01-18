package com.examly.springapp.service;

import java.util.List;

import com.examly.springapp.model.Review;

public interface AiReviewSummaryService {
    String generateSummary(List<Review> reviews);

}
