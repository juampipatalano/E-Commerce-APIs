package com.uade.tpo.demo.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.uade.tpo.demo.entity.OrderDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Long> {
    @Query("SELECT o FROM OrderDetail o WHERE o.order.id = ?1 and o.product.id = ?2")
    Optional<OrderDetail> getOrderDetailByOrderAndProductId(Long orderId, Long productId);


}