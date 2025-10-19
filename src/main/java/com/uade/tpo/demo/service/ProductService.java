package com.uade.tpo.demo.service;

import java.math.BigDecimal;
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
    public Product createProduct(String name, String description, Category category, BigDecimal price, Integer stock, byte[] image, BigDecimal descuento) throws ProductDuplicateException;
    public Page<Product> getProductsByCategory(Long categoryId, PageRequest pageRequest);
    public Product updateProduct(Long productId, String name, String description, Category category, BigDecimal price, Integer stock, byte[] image, BigDecimal descuento) throws ProductNotFoundException;
    public void deleteProduct(Long productId) throws ProductNotFoundException;
    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, PageRequest pageRequest) throws ProductNotFoundException;
    public Page<Product> getDiscountedProducts(PageRequest pageRequest);
    public Page<Product> getAllProducts(PageRequest pageRequest);

}

