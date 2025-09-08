package com.uade.tpo.demo.entity;

import java.math.BigDecimal;
import java.time.LocalDate;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Data
@Table(name = "orders")
public class Order {

    public Order() {
    }

    public Order(LocalDate date, BigDecimal totalPrice, String shippingAddress, String paymentMethod, User user) {
        this.date = date;
        this.totalPrice = totalPrice;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private BigDecimal totalPrice;

    @Column
    private String shippingAddress;

    @Column
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @JsonManagedReference
    @OneToMany (mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
