package com.uade.tpo.demo.entity;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
@Entity
@Table(name = "products")
public class Product {

    public Product() {
    }

    public Product(String name, String description, Category category, BigDecimal price, Integer stock, String imageUrl, BigDecimal discount)
 {
        this.name = name;
        this.description = description;
        this.category= category;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.discount = discount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonBackReference
    private Category category;

    @Positive
    @Column
    private BigDecimal price;

    @Positive
    @Column
    private Integer stock;

    @Column
    private String imageUrl;


    @Positive // mayor que 0
    @DecimalMax(value = "1.0", inclusive = false) // menor que 1
    @Column
    private BigDecimal discount;


    @Column(nullable = false)
    private Boolean active = true; //Se crea el producto siempre en estado activo


    public void decreaseStock(){
        if (this.stock <= 0) {
            throw new IllegalStateException("No hay stock disponible para el producto: " + this.name);
        }else{
        this.stock -= 1;
        }
    }
}
 