package com.uade.tpo.demo.controllers.products;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.demo.controllers.responses.MessageResponse;
import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.exceptions.ProductDuplicateException;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import com.uade.tpo.demo.service.CategoryService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/by-price")
    public ResponseEntity<Page<Product>> getProductsByPriceRange(@RequestParam BigDecimal minPrice, @RequestParam BigDecimal maxPrice,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        if (page == null || size == null) {
            return ResponseEntity.ok(productService.getProductsByPriceRange(minPrice, maxPrice, PageRequest.of(0, Integer.MAX_VALUE)));
        }
        return ResponseEntity.ok(productService.getProductsByPriceRange(minPrice, maxPrice, PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(
        @RequestPart("product") ProductsRequest productsRequest,
        @RequestPart("image") MultipartFile imageFile)
            throws ProductDuplicateException, IOException{
        Category category = categoryService.getCategoryById(productsRequest.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Product result = productService.createProduct(productsRequest.getName(), 
                                                    productsRequest.getDescription(), 
                                                    category,
                                                    productsRequest.getPrice(), 
                                                    productsRequest.getStock(), 
                                                    imageFile.getBytes(),
                                                    productsRequest.getDiscount());
                

        MessageResponse response = new MessageResponse("Se ha creado el producto de manera exitosa");  
        return ResponseEntity.created(URI.create("/products/" + result.getId())).body(response);
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long productId) throws ProductNotFoundException{
        
            productService.deleteProduct(productId);
            return ResponseEntity.ok().build();
        }
        
        
    

    @PutMapping("/{productId}")
    public ResponseEntity<MessageResponse> updateProduct(
        @PathVariable Long productId, 
        @RequestPart("product") ProductsRequest productsRequest,
        @RequestPart(value = "image", required = false) MultipartFile imageFile) 
        throws ProductNotFoundException, IOException{
        Category category = categoryService.getCategoryById(productsRequest.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            byte[] imageBytes = (imageFile != null && !imageFile.isEmpty()) ? imageFile.getBytes() : null;
            Product updatedProduct = productService.updateProduct(productId,
                                                                productsRequest.getName(), 
                                                                productsRequest.getDescription(), 
                                                                category,
                                                                productsRequest.getPrice(), 
                                                                productsRequest.getStock(), 
                                                                imageBytes,
                                                                productsRequest.getDiscount());


            MessageResponse response = new MessageResponse("Se ha actualizado el producto de manera exitosa");                                    
            return(ResponseEntity.ok(response));
        
        

        

    }

    @GetMapping("/sorted-by-price")
    public ResponseEntity<Page<Product>> getProductsSortedByPrice(
            @RequestParam(defaultValue = "asc") String order,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        PageRequest pageRequest;
        if (page == null || size == null) {
            pageRequest = PageRequest.of(0, Integer.MAX_VALUE,
                    order.equalsIgnoreCase("desc")
                            ? org.springframework.data.domain.Sort.by("price").descending()
                            : org.springframework.data.domain.Sort.by("price").ascending());
        } else {
            pageRequest = PageRequest.of(page, size,
                    order.equalsIgnoreCase("desc")
                            ? org.springframework.data.domain.Sort.by("price").descending()
                            : org.springframework.data.domain.Sort.by("price").ascending());
        }
        return ResponseEntity.ok(productService.getProducts(pageRequest));
    }

    @GetMapping("/discounted")
    public ResponseEntity<Page<Product>> getDiscountedProducts() {
        // Orden descendente por descuento
        PageRequest pageRequest = PageRequest.of(
            0,                     // primera página
            Integer.MAX_VALUE,     // todos los productos
            org.springframework.data.domain.Sort.by("discount").descending()
        );

        return ResponseEntity.ok(productService.getDiscountedProducts(pageRequest));
    }

   

}
