package com.uade.tpo.demo.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Product;

import io.jsonwebtoken.security.Jwks.OP;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    /*@Query(value = "select p from Product p where p.name = ?1")
    List<Product> findByName(String name);*/

    @Query("SELECT p FROM Product p WHERE p.stock > 0 and p.name = ?1")
    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE p.stock > 0 and p.category.id = ?1")
    Page<Product> findByCategoryId(Long categoryId, PageRequest pageRequest);

    @Query("SELECT p FROM Product p WHERE p.stock > 0")
    Page<Product> findInStock(PageRequest pageRequest);

    @Query("SELECT p FROM Product p WHERE p.stock > 0 and p.id = ?1")
    Optional<Product> findByIdInStock(Long productId);

    //filtrar productos por rango de precio
    @Query("SELECT p FROM Product p WHERE p.stock > 0 and (p.price BETWEEN :minPrice AND :maxPrice)")
    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, PageRequest pageRequest);

}

