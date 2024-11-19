package com.sarthak.mycart.services;

import com.sarthak.mycart.entities.CartItem;

public interface CartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);
    CartItem getCartItemByProductAndCart(Long cartId, Long productId);
}
