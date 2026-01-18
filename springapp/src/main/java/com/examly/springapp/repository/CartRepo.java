package com.examly.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.model.Cart;

public interface CartRepo extends JpaRepository<Cart,Long> {
    
}
