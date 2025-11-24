package com.uade.tpo.demo.entity;

import java.math.BigDecimal;
import java.util.Base64;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Data
@Entity
@Table(name = "products")
public class Product {

    public Product() {
    }

    public Product(String name, String description, Category category, BigDecimal price, Integer stock, byte[] image, BigDecimal discount)
 {
        this.name = name;
        this.description = description;
        this.category= category;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.discount = discount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column
    private String name;

    @NotBlank
    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonBackReference
    private Category category;

    @JsonProperty("categoryId")
    public Long getCategoryId() {
        return (this.category != null) ? this.category.getId() : null;
    }

   
    @Positive
    @Column
    private BigDecimal price;


    @Min(0)
    @Column
    private Integer stock;

    @Lob
    @Column (columnDefinition = "LONGBLOB")
    @JsonIgnore
    private byte[] image;


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

    public String getImageBase64(){ //Cuando se envía el producto como JSON, la imagen se envía en Base64
        if(this.image != null){
            return Base64.getEncoder().encodeToString(this.image);
        }
        return null;
    }
}
 