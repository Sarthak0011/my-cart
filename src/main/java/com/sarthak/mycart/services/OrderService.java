package com.sarthak.mycart.services;

import com.sarthak.mycart.entities.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(Long userId);

    Order getOrder(Long id);

    List<Order> getUserOrders(Long userId);
}
