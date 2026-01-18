package com.examly.springapp.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.examly.springapp.model.Review;

@Service
public class AiReviewSummaryServiceImpl implements AiReviewSummaryService {
    
    @Value("${openai.model}")
    private String model;
    
    private final WebClient webClient;
    
    private static final String SYSTEM_PROMPT = "You are a helpful product review analyzer. Analyze customer reviews and provide a concise, descriptive summary that highlights the overall product quality and key feedback points.";

    public AiReviewSummaryServiceImpl(@Value("${openai.api.key}") String apiKey,
    @Value("${openai.api.url}") String openaiUrl,
    @Value("${openai.model}") String model) {

        this.model = model;
        this.webClient = WebClient.builder()
                .baseUrl(openaiUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    
    
    private static class OpenAiChatResponse {
        private List<Choice> choices;
        public List<Choice> getChoices() { return choices; }
        public void setChoices(List<Choice> choices) { this.choices = choices; }
        
        static class Choice {
            private Message message;
            public Message getMessage() { return message; }
            public void setMessage(Message message) { this.message = message; }
        }
        
        static class Message {
            private String role;
            private String content;
            public String getRole() { return role; }
            public void setRole(String role) { this.role = role; }
            public String getContent() { return content; }
            public void setContent(String content) { this.content = content; }
        }
    }
    
    @Override
    public String generateSummary(List<Review> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            return "No reviews available yet.";
        }
     
        // Calculate average rating
        double avgRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
        
        StringBuilder reviewsText = new StringBuilder();
        reviewsText.append("Product Reviews Analysis:\n");
        reviewsText.append("Total Reviews: ").append(reviews.size()).append("\n");
        reviewsText.append("Average Rating: ").append(String.format("%.1f", avgRating)).append("/5\n\n");
        reviewsText.append("Customer Feedback:\n\n");
     
        for (int i = 0; i < reviews.size(); i++) {
            Review review = reviews.get(i);
            reviewsText.append((i + 1))
                       .append(". [Rating: ")
                       .append(review.getRating())
                       .append("/5] ")
                       .append(review.getUser() != null ? review.getUser().getUsername() : "Anonymous")
                       .append(" says: \"")
                       .append(review.getContent())
                       .append("\"\n\n");
        }
     
        String prompt = """
            You are an expert product review analyst. Based on the customer reviews and ratings provided below, generate a natural, conversational summary in 2-3 sentences.
            
            Your summary should:
            - Start with an overall quality assessment (e.g., "This product is excellent/good/average/poor")
            - Mention what customers specifically praise or criticize (build quality, features, value, performance, etc.)
            - Be specific about the product aspects mentioned in reviews
            - Sound natural and descriptive, not formulaic
            
            Examples of good summaries:
            - "This product is excellent and customers consistently praise its exceptional build quality, durable materials, and reliable performance. Users particularly appreciate the attention to detail in design and the product's longevity."
            - "This product is average with mixed feedback. While some customers appreciate the affordable price and basic functionality, many reviews highlight concerns about poor build quality, cheap materials, and inconsistent performance."
            - "This product is poor and reviews consistently mention significant quality issues. Customers report problems with durability, subpar materials, and overall disappointing performance that doesn't meet expectations."
            
            Now analyze these reviews:
            
            """ + reviewsText.toString();
     
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", SYSTEM_PROMPT));
        messages.add(Map.of("role", "user", "content", prompt));
        
        body.put("messages", messages);
        body.put("temperature", 0.4);
        body.put("max_tokens", 200);
     
        try {
            OpenAiChatResponse resp = webClient.post()
                    .body(BodyInserters.fromValue(body))
                    .retrieve()
                    .bodyToMono(OpenAiChatResponse.class)
                    .block(Duration.ofSeconds(40));
     
            return resp != null && resp.getChoices() != null && !resp.getChoices().isEmpty()
                    ? resp.getChoices().get(0).getMessage().getContent().trim()
                    : "No summary generated.";
        } catch (Exception e) {
            e.printStackTrace();
            return "AI summarizer temporarily unavailable. Please try again later.";
        }
    }
}