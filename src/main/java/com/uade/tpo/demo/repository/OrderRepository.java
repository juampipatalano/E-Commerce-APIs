package com.uade.tpo.demo.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uade.tpo.demo.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    
    @Query("SELECT o FROM Order o WHERE o.user.id = ?1")
    List<Order> findByUserId(Long userId);

}