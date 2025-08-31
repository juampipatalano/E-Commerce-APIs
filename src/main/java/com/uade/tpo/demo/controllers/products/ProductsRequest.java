package com.uade.tpo.demo.controllers.products;
import com.uade.tpo.demo.entity.Category;
import lombok.Data;

@Data
public class ProductsRequest {
    private int id;
    private String name;
    private String description;
    private Category category;
    private Double price;
    private Integer stock;
    private String imageUrl;
   
}
