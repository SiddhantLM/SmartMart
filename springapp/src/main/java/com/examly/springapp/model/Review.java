package com.examly.springapp.model;
 
import com.fasterxml.jackson.annotation.JsonBackReference;
 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
 
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;
    private String content;
    private int rating;
 
    @ManyToOne
    private User user;
 
    @ManyToOne
    @JsonBackReference
    private Product product;
 
    public Review(Long reviewId, String content, int rating, User user, Product product) {
        this.reviewId = reviewId;
        this.content = content;
        this.rating = rating;
        this.user = user;
        this.product = product;
    }
 
    public Review () {}
 
    public Long getReviewId() {
        return reviewId;
    }
 
    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }
 
    public String getContent() {
        return content;
    }
 
    public void setContent(String content) {
        this.content = content;
    }
 
    public int getRating() {
        return rating;
    }
 
    public void setRating(int rating) {
        this.rating = rating;
    }
 
    public User getUser() {
        return user;
    }
 
    public void setUser(User user) {
        this.user = user;
    }
 
    public Product getProduct() {
        return product;
    }
 
    public void setProduct(Product product) {
        this.product = product;
    }
 
   
}