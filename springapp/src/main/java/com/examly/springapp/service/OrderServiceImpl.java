package com.examly.springapp.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Address;
import com.examly.springapp.model.CartItem;
import com.examly.springapp.model.Order;
import com.examly.springapp.model.OrderItem;
import com.examly.springapp.model.User;
import com.examly.springapp.model.Order.OrderStatus;
import com.examly.springapp.repository.AddressRepo;
import com.examly.springapp.repository.OrderRepo;
import com.examly.springapp.repository.UserRepo;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private CartService cartService;

    @Override
    public Order addOrder(Long userId, Long cartId, Long addressId) {
        User user = userRepo.findById(userId).orElse(null);
        Address address = addressRepo.findById(addressId).orElse(null);

        if(user!=null && address!=null) {
            Order newOrder = new Order();
            List<OrderItem> newItems = new ArrayList<>();
            
            newOrder.setAddress(address);
            newOrder.setQuantity(user.getCart().getQuantity());
            newOrder.setTotalAmount(user.getCart().getTotalAmount());
            newOrder.setUser(user);
            newOrder.setStatus(OrderStatus.PENDING);
            
            for (CartItem c : user.getCart().getItems()) {
                if(c.getQuantity() > c.getProduct().getStock()) {
                    throw new RuntimeException("Stock Unavailable");
                }
                newItems.add(new OrderItem(c.getProduct(), c.getQuantity()));
                c.getProduct().setStock((c.getProduct().getStock() - c.getQuantity()));
            }
            
            newOrder.setItems(newItems);
            newOrder.setCreatedAt(LocalDate.now());
            newOrder.setUpdatedAt(LocalDate.now());
            cartService.resetCart(cartId);
            return orderRepo.save(newOrder);
        }

        return null;
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepo.deleteById(orderId);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepo.findById(orderId).orElse(null);
    }

    @Override
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepo.findByUser_UserId(userId);
    }

    @Override
    public List<Order> getOrdersByStatus(Order.OrderStatus status) {
        return orderRepo.findByStatus(status);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public Order updateShippingAddress(Long orderId, Long addressId) {
        Order order = orderRepo.findById(addressId).orElse(null);
        Address address = addressRepo.findById(addressId).orElse(null);

        if(order != null && address!=null) {
            order.setAddress(address);
            return orderRepo.save(order);
        }

        return null;
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepo.findById(orderId).orElse(null);

        if(order!=null) {
            order.setStatus(OrderStatus.valueOf(status));
            return orderRepo.save(order);
        }

        return null;
    }
    
}
