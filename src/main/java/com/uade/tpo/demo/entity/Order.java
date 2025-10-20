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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Data
@Table(name = "orders")
public class Order {

    public Order() {
    }

    public Order(BigDecimal totalPrice, String shippingAddress, String paymentMethod, User user) {
        this.date = LocalDate.now();
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


    @NotBlank
    @Column
    private String shippingAddress;

    @NotBlank
    @Column
    private String paymentMethod;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;
    @JsonProperty("userId")
    public Long getUserId() {
        return (this.user != null) ? this.user.getId() : null;
    }
    

    @JsonManagedReference
    @OneToMany (mappedBy = "order")
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
