package com.uade.tpo.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "products")
public class Product {

    public Product() {
    }

    public Product(String name, String description, Category category, Double price, Integer stock, String imageUrl) {
        this.name = name;
        this.description = description;
        this.category= category;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
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

    @Column
    private Double price;

    @Column
    private Integer stock;

    @Column
    private String imageUrl;
    public void decreaseStock(){
        if (this.stock <= 0) {
            throw new IllegalStateException("No hay stock disponible para el producto: " + this.name);
        }else{
        this.stock -= 1;
        }
    }
}
