package com.sarthak.mycart.repositories;

import com.sarthak.mycart.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long cartId);
}
