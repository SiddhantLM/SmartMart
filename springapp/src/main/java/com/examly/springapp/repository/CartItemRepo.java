package com.examly.springapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.examly.springapp.model.CartItem;

public interface CartItemRepo extends JpaRepository<CartItem,Long> {
    
}
