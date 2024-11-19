package com.sarthak.mycart.services.impl;

import com.sarthak.mycart.entities.Cart;
import com.sarthak.mycart.entities.CartItem;
import com.sarthak.mycart.exceptions.ResourceNotFoundException;
import com.sarthak.mycart.repositories.CartItemRepository;
import com.sarthak.mycart.repositories.CartRepository;
import com.sarthak.mycart.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Cart getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Cart with id %d", cartId)));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cart;
    }

    @Override
    public void clearCart(Long cartId) {
        Cart cart = getCart(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        cart.getCartItems().clear();
        cartRepository.deleteById(cartId);
    }

    @Override
    public BigDecimal getTotalPrice(Long cartId) {
        Cart cart = getCart(cartId);
        return cart.getTotalAmount();
    }
}
