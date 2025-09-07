package com.uade.tpo.demo.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.uade.tpo.demo.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    /*@Query(value = "select p from Product p where p.name = ?1")
    List<Product> findByName(String name);*/

    @Query("SELECT p FROM Product p WHERE p.name = ?1")
    Optional<Product> findByName(String name);

    @Query("SELECT p FROM Product p WHERE p.category.id = ?1")
    Page<Product> findByCategoryId(Long categoryId, PageRequest pageRequest);


}

