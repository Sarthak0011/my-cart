package com.sarthak.mycart.services;

import com.sarthak.mycart.entities.Cart;

import java.math.BigDecimal;

public interface CartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);
}
