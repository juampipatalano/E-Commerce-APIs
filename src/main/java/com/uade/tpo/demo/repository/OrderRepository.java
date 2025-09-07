package com.uade.tpo.demo.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.demo.entity.Order;
import com.uade.tpo.demo.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {


}