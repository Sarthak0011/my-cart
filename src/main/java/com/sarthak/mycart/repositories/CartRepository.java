package com.sarthak.mycart.repositories;

import com.sarthak.mycart.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long user_id);
}
