package com.examly.springapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.examly.springapp.model.Cart;
import com.examly.springapp.service.CartService;

@RestController
public class CartController {
    @Autowired
    CartService service;

    @PostMapping("/api/cart/{cartId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Cart> updateCart (@PathVariable Long cartId,@RequestBody Cart cart) {
        try {
            return new ResponseEntity<>(service.updateCart(cartId, cart), HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    @PutMapping("/api/cart/{cartId}/reset")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Boolean> resetCart (@PathVariable Long cartId) {
        try {
            return new ResponseEntity<>(service.resetCart(cartId), HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }


    @PreAuthorize("hasRole('USER')")
    @PutMapping("/api/cart/{cartId}/item/{itemId}")
    public ResponseEntity<Cart> removeItem (@PathVariable Long cartId, @PathVariable Long itemId) {
        try {
            return new ResponseEntity<> (service.removeItem(cartId,itemId), HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }
}
