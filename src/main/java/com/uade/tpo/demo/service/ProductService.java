package com.uade.tpo.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.exceptions.ProductDuplicateException;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import org.springframework.data.domain.Page;

public interface ProductService {
    
    public Page<Product> getProducts(PageRequest pageRequest);
    public Optional<Product> getProductById(Long productId);
    public Product createProduct(String name, String description, Category category, Double price, Integer stock, String imageUrl) throws ProductDuplicateException;
    public Page<Product> getProductsByCategory(Long categoryId, PageRequest pageRequest);
    public Product updateProduct(Long productId, String name, String description, Category category, Double price, Integer stock, String imageUrl) throws ProductNotFoundException;
    public void deleteProduct(Long productId) throws ProductNotFoundException;
   
}

