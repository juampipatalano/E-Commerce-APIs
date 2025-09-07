package com.uade.tpo.demo.controllers.orders;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;

import com.uade.tpo.demo.entity.Order;

import com.uade.tpo.demo.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrdersController{
    @Autowired
    private OrderService orderService;


    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long orderId){
        Optional<Order> result = orderService.getOrderById(orderId);
        if (result.isPresent()){
            Order order = result.get();
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.notFound().build();
        }
        
    }
    
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrdersRequest ordersRequest){
        Order result = orderService.createOrder(ordersRequest.getDate(), 
                                                ordersRequest.getShippingAddress(), 
                                                ordersRequest.getPaymentMethod(), 
                                                ordersRequest.getTotalPrice(), 
                                                ordersRequest.getProductsId(), 
                                                ordersRequest.getUserId());
                                                
        return ResponseEntity.created(URI.create("/orders/" + result.getId())).body(result);
    }


    
    @DeleteMapping
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId){
        
        //orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }


    
    

}