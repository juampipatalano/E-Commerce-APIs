package com.uade.tpo.demo.service;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.exceptions.ProductDuplicateException;
import com.uade.tpo.demo.exceptions.ProductNotFoundException;
import com.uade.tpo.demo.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
 
     @Autowired
     private ProductRepository productRepository;
 
     @Override
     public Page<Product> getProducts(PageRequest pageRequest) {
         return productRepository.findAll(pageRequest);
     }
 
     @Override
     public Optional<Product> getProductById(Long productId) {
         return productRepository.findById(productId);
     }
 
     @Override
     public Product createProduct(String name, String description, Category category, Double price, Integer stock, String imageUrl) throws ProductDuplicateException {
            if(productRepository.findByName(name).isEmpty()) {
                Product product = new Product(name, description, category, price, stock, imageUrl);
                return productRepository.save(product);
            }
            throw new ProductDuplicateException();
     }
    

	 @Override
	 public Page<Product> getProductsByCategory(Long categoryId, PageRequest pageRequest) {
        return productRepository.findByCategoryId (categoryId, pageRequest);
        
	 }


    @Transactional
    @Override
    public void deleteProduct(Long id){
        if(productRepository.findById(id) == null){
            throw new ProductNotFoundException("El producto con " + id + " no existe");
        }
        productRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Product updateProduct(Long productId, String name, String description, Category category, Double price, Integer stock,
            String imageUrl) throws ProductNotFoundException { //XQ DICE THROWS... Y EL DE ARRIBA NO ?-ademas lo validamos dos veces (aca y en el controller)
        
        Optional<Product> productOpt= productRepository.findById(productId);
        if(productOpt.isPresent()){
            Product product = productOpt.get();
            product.setId(productId);
            product.setName (name);
            product. setDescription (description);
            product.setCategory (category);
            product.setPrice (price);
            product.setStock (stock);
            product.setImageUrl (imageUrl);
            
            return productRepository.save(product);
        }

        throw new ProductNotFoundException("El producto con id " + productId + " no existe");

        
        
    }

	

 
 }