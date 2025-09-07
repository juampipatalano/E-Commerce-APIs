package com.uade.tpo.demo.service;
import com.uade.tpo.demo.entity.Order;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface OrderService {
    Optional<Order> getOrderById(Long id);
    Order createOrder(LocalDate date, String shippingAddress, String paymentMethod, List<Long> productsId, Long userId);
    Page<Order> getOrders(PageRequest pageRequest);

}
