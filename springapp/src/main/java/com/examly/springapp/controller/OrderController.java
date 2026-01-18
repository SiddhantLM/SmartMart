package com.examly.springapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.examly.springapp.model.Order;
import com.examly.springapp.model.OrderRequestDTO;
import com.examly.springapp.model.Order.OrderStatus;
import com.examly.springapp.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/user/{userId}/cart/{cartId}")
    public ResponseEntity<Order> addOrder(@RequestBody OrderRequestDTO addressId, @PathVariable Long userId, @PathVariable Long cartId) {
        try {
            System.out.println(addressId);
            Order savedOrder = orderService.addOrder(userId, cartId, addressId.getAddressId());
            return new ResponseEntity<>(savedOrder,HttpStatus.valueOf(200)); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.valueOf(403)); 
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order != null) {
            return new ResponseEntity<>(order,HttpStatus.valueOf(200)); 
        } else {
            return new ResponseEntity<>("not done", HttpStatus.valueOf(404));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        Order existingOrder = orderService.getOrderById(id);
        if (existingOrder != null) {
            updatedOrder.setOrderId(id);
            // Order savedOrder = orderService.addOrder(updatedOrder);
            return new ResponseEntity<>( HttpStatus.valueOf(200)); 
        } else {
            return new ResponseEntity<>("not done",HttpStatus.valueOf(500)); 
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable Long id) {
        Order existingOrder = orderService.getOrderById(id);
        if (existingOrder != null) {
            orderService.deleteOrder(id);
            return new ResponseEntity<>("deleted",HttpStatus.valueOf(200)); 
        } else {
            return new ResponseEntity<>("not done",HttpStatus.valueOf(500)); 
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus (@PathVariable Long id,@RequestBody Map<String,String> payload) {
        try {
            Order order = orderService.updateOrderStatus(id, payload.get("status"));
            return new ResponseEntity<>(order, HttpStatusCode.valueOf(200));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatusCode.valueOf(500));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?>getAllOrders() {
        try {
            List<Order> orders = orderService.getAllOrders();
            return new ResponseEntity<>(orders, HttpStatus.valueOf(200));
        } catch (Exception e) {
            return new ResponseEntity<>("not done",HttpStatus.valueOf(500));
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getOrdersByUser(@PathVariable Long userId) {
        List<Order> orders = orderService.getOrdersByUser(userId);
        if (!orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.valueOf(200)); 
        } else {
            return new ResponseEntity<>("not done" ,HttpStatus.valueOf(500)); 
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<Order> orders = orderService.getOrdersByStatus(status);
        if (!orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.valueOf(200));
        } else {
            return new ResponseEntity<>(HttpStatus.valueOf(500));
        }
    }
}