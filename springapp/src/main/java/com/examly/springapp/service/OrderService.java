package com.examly.springapp.service;

import java.util.List;
import com.examly.springapp.model.Order;

public interface OrderService {
    Order addOrder(Long userId, Long cartId, Long addressId);
    void deleteOrder(Long orderId);
    Order getOrderById(Long orderId);
    Order updateShippingAddress(Long orderId,Long addressId);
    List<Order> getOrdersByUser(Long userId);
    List<Order> getOrdersByStatus(Order.OrderStatus status);
    List<Order> getAllOrders();
    Order updateOrderStatus (Long orderId, String status);
}
