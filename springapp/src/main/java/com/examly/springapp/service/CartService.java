package com.examly.springapp.service;

import com.examly.springapp.model.Cart;

public interface CartService {
    Cart updateCart (Long cartId, Cart newCart);
    boolean resetCart (Long cartId);
    Cart removeItem (Long cartId, Long cartItemId);
}
