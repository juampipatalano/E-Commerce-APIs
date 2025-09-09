package com.uade.tpo.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.demo.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "select c from Category c where c.description = ?1")
    List<Category> findByDescription(String description);

    @Query("select c from Category c where c.id = ?1 and c.active = true")
    Optional<Category> findActiveById(Long id);

    @Query("select c from Category c where c.active = true")
    Page<Category> findAllActive(PageRequest pageable);
    
}
