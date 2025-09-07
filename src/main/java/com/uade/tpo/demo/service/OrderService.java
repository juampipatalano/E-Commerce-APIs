package com.uade.tpo.demo.service;
import com.uade.tpo.demo.entity.Order;

import java.time.LocalDate;

import com.uade.tpo.demo.controllers.orders.OrdersRequest;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    Optional<Order> getOrderById(Long id);
    Order createOrder(LocalDate date, String shippingAddress, String paymentMethod, Double totalPrice, List<Long> productsId, Long userId);
    void deleteOrder(Long orderId, Long userId);
}
