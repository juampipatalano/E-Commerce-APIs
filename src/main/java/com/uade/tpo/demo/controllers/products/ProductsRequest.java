package com.uade.tpo.demo.controllers.products;
import lombok.Data;

@Data
public class ProductsRequest {
    private int id;
    private String name;
    private String description;
    private Long categoryId; // <-- solo el ID, luego busco dentro del controller a la categoria
    private Double price;
    private Integer stock;
    private String imageUrl;
   
}
