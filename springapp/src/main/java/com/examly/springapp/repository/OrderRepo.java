
package com.examly.springapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.examly.springapp.model.Order;

public interface OrderRepo extends JpaRepository<Order,Long>{
    List<Order> findByUser_UserId(Long userId);
    List<Order> findByStatus(Order.OrderStatus status);
}
