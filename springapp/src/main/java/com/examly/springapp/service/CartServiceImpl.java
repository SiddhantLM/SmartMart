package com.examly.springapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.examly.springapp.model.Cart;
import com.examly.springapp.model.CartItem;
import com.examly.springapp.model.Product;
import com.examly.springapp.repository.CartItemRepo;
import com.examly.springapp.repository.CartRepo;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartRepo cartRepo;

    @Autowired
    CartItemRepo cartItemRepo;

    @Override
    public Cart updateCart(Long cartId, Cart newCart) {
        Cart cart = cartRepo.findById(cartId).orElse(null);

        if(cart!=null) {
            List<CartItem> items = newCart.getItems();
            for(CartItem item : items) {
                item.setCart(cart);         //for cart id = null
            }
            cart.setItems(items);
            int totalQuantity = 0;
            double amount = 0;

            for(CartItem c:items) {
                totalQuantity+=c.getQuantity();
                amount+=(c.getProduct().getPrice()*c.getQuantity()); //multiply by quatity
            }

            cart.setQuantity(totalQuantity);
            cart.setTotalAmount(amount);

            return cartRepo.save(cart);
        }

        return null;
    }

    @Override
    public boolean resetCart(Long cartId) {
        Cart cart = cartRepo.findById(cartId).orElse(null);

        if(cart!=null) {

            for(CartItem c:cart.getItems()) {
                c.setCart(null);
                cartItemRepo.delete(c);
            }

            cart.setItems(new ArrayList<>());
            cart.setQuantity(0);
            cart.setTotalAmount(0);

            cartRepo.save(cart);
        }

        return false;
    }

    @Override
    public Cart removeItem(Long cartId, Long cartItemId) {
        Cart cart = cartRepo.findById(cartId).orElse(null);
        CartItem cartItem = cartItemRepo.findById(cartItemId).orElse(null);

        if(cart!=null && cartItem!=null) {
            List<CartItem> items = cart.getItems();
            items.remove(cartItem);
            cart.setItems(items);
            cartItem.setCart(null);
            cartItemRepo.delete(cartItem);

            int totalQuantity = 0;
            double amount = 0;

            for(CartItem c:items) {
                totalQuantity+=c.getQuantity();
                amount+=(c.getProduct().getPrice()*c.getQuantity()); //multiply by quatity
            }

            cart.setQuantity(totalQuantity);
            cart.setTotalAmount(amount);

            return cartRepo.save(cart);
        }

        return null;
    }    
}
