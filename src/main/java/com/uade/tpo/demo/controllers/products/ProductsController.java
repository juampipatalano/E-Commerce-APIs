package com.uade.tpo.demo.controllers.products;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.exceptions.ProductDuplicateException;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import com.uade.tpo.demo.service.CategoryService;


import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.uade.tpo.demo.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductsController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size){
            if (page == null || size == null) {
                return ResponseEntity.ok(productService.getProducts(PageRequest.of(0, Integer.MAX_VALUE)));
            }
            return ResponseEntity.ok(productService.getProducts(PageRequest.of(page, size)));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Optional<Product> result = productService.getProductById(productId);
        if (result.isPresent())
            return ResponseEntity.ok(result.get());

        return ResponseEntity.noContent().build();
    }


    @PostMapping
    public ResponseEntity<Object> createProduct(@RequestBody ProductsRequest productsRequest)
            throws ProductDuplicateException{
        Category category = categoryService.getCategoryById(productsRequest.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Product result = productService.createProduct(productsRequest.getName(), 
                                                    productsRequest.getDescription(), 
                                                    category,
                                                    productsRequest.getPrice(), 
                                                    productsRequest.getStock(), 
                                                    productsRequest.getImageUrl());

        
        return ResponseEntity.created(URI.create("/products/" + result.getId())).body(result);
    }

    @GetMapping("/category/{categoryId}/products")
    public ResponseEntity<Page<Product>> getProductsByCategory(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @PathVariable Long categoryId){
            if (page == null || size == null) {
                return ResponseEntity.ok(productService.getProductsByCategory(categoryId, PageRequest.of(0, Integer.MAX_VALUE)));
            }
            return ResponseEntity.ok(productService.getProductsByCategory(categoryId, PageRequest.of(page, size)));
    
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long productId) 
        throws ProductNotFoundException {
        Optional<Product> result = productService.getProductById(productId);
        if (result.isPresent()){
            productService.deleteProduct(productId);
            return ResponseEntity.ok().build();
        }
        throw new ProductNotFoundException("Producto no encontrado con id: " + productId);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody ProductsRequest productsRequest) 
        throws ProductNotFoundException {
        Category category = categoryService.getCategoryById(productsRequest.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Optional<Product> result = productService.getProductById(productId);
        if (result.isPresent()) {
            Product updatedProduct = productService.updateProduct(productId,
                                                                productsRequest.getName(), 
                                                                productsRequest.getDescription(), 
                                                                category,
                                                                productsRequest.getPrice(), 
                                                                productsRequest.getStock(), 
                                                                productsRequest.getImageUrl());
            ResponseEntity.ok(updatedProduct);
        }
        throw new ProductNotFoundException("Producto no encontrado con id: " + productId);

    }





}
