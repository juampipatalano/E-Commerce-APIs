package com.uade.tpo.demo.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.uade.tpo.demo.entity.Product;

import io.jsonwebtoken.security.Jwks.OP;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("SELECT p FROM Product p WHERE p.stock > 0 and p.name = ?1")
    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE p.stock > 0 and p.category.id = ?1 and p.active = true")
    Page<Product> findByCategoryId(Long categoryId, PageRequest pageRequest);

    @Query("SELECT p FROM Product p WHERE p.stock > 0 and p.active=true")
    Page<Product> findInStock(PageRequest pageRequest);

    @Query("SELECT p FROM Product p WHERE p.stock > 0 and p.id = ?1 and p.active=true")
    Optional<Product> findByIdInStock(Long productId);

    //filtrar productos por rango de precio

    /*@Query("SELECT p FROM Product p WHERE p.stock > 0 and (p.price BETWEEN :minPrice AND :maxPrice) and p.active = true")
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, PageRequest pageRequest);*/
    // Reemplazá esto en ProductRepository

    @Query("""
        SELECT p FROM Product p 
        WHERE p.stock > 0 
        AND p.active = true 
        AND (
            CASE 
                WHEN (p.discount IS NOT NULL AND p.discount > 0) 
                THEN (p.price * (1 - p.discount)) 
                ELSE p.price 
            END
        ) BETWEEN :minPrice AND :maxPrice
    """)
    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, PageRequest pageRequest);

    @Query("""
       SELECT p FROM Product p
       WHERE p.active = true
         AND p.stock > 0
         AND p.discount IS NOT NULL
         AND p.discount > 0
       """)
    Page<Product> findDiscountedInStock(PageRequest pageRequest);

    // 1. Ordenar ASC (Menor a Mayor) considerando descuento
    @Query("""
        SELECT p FROM Product p
        WHERE p.active = true AND p.stock > 0
        ORDER BY (
            CASE 
                WHEN (p.discount IS NOT NULL AND p.discount > 0) 
                THEN (p.price * (1 - p.discount)) 
                ELSE p.price 
            END
        ) ASC
    """)
    Page<Product> findAllSortedByRealPriceAsc(PageRequest pageRequest);

    // 2. Ordenar DESC (Mayor a Menor) considerando descuento
    @Query("""
        SELECT p FROM Product p
        WHERE p.active = true AND p.stock > 0
        ORDER BY (
            CASE 
                WHEN (p.discount IS NOT NULL AND p.discount > 0) 
                THEN (p.price * (1 - p.discount)) 
                ELSE p.price 
            END
        ) DESC
    """)
    Page<Product> findAllSortedByRealPriceDesc(PageRequest pageRequest);

}

