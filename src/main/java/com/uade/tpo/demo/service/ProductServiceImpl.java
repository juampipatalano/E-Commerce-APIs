package com.uade.tpo.demo.service;
import java.math.BigDecimal;
import java.util.Optional;

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
        public Page<Product> getProducts(PageRequest pageRequest) {//modificamos para que traiga solo los productos con stock 
         return productRepository.findInStock(pageRequest);
         
     }

     @Override
     public Optional<Product> getProductById(Long productId) {
         return productRepository.findByIdInStock(productId);//modificamos para que traiga solo los productos con stock 
     }
     
     @Transactional
     @Override
     public Product createProduct(String name, String description, Category category, BigDecimal price, Integer stock, String imageUrl, BigDecimal discount) 
     throws ProductDuplicateException {
            if(productRepository.findByName(name).isEmpty()) {
                Product product = new Product(name, description, category, price, stock, imageUrl, discount);
                return productRepository.save(product);
            }
            throw new ProductDuplicateException();
     }
    
	 @Override
	 public Page<Product> getProductsByCategory(Long categoryId, PageRequest pageRequest) {
        return productRepository.findByCategoryId (categoryId, pageRequest);
        
	 }

     @Override
    public Page<Product> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, PageRequest pageRequest) {
        return productRepository.findByPriceBetween(minPrice, maxPrice, pageRequest);
    }


    @Transactional
    @Override
    public void deleteProduct(Long id) throws ProductNotFoundException {
        Optional<Product> result = productRepository.findById(id);
        if( result == null){
            throw new ProductNotFoundException("El producto con " + id + " no existe");
        }
        else{//Necesito el producto en mi db debido a que está relacionado con los detalles de las órdenes 
                                                //no puedo eliminarlo. Se setea en false.
            Product producto = result.get();
            producto.setActive(false);
            productRepository.save(producto);
        }
    }

    @Transactional
    @Override
    public Product updateProduct(Long productId, String name, String description, Category category, BigDecimal price, Integer stock,
            String imageUrl, BigDecimal descuento) throws ProductNotFoundException { 
        
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
            product.setDiscount(descuento);
            product.setActive(true);
            
            return productRepository.save(product);
        } 
        else{
            throw new ProductNotFoundException("El producto con id " + productId + " no existe");
        }

    }


 }