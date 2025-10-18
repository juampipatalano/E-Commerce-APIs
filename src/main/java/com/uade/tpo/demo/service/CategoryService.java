
package com.uade.tpo.demo.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;
import com.uade.tpo.demo.exceptions.CategoryNotFoundException;

public interface CategoryService {
    public Page<Category> getCategories(PageRequest pageRequest);

    public Optional<Category> getCategoryById(Long categoryId);

    public Category createCategory(String description) throws CategoryDuplicateException;

    public void deleteCategory(Long categoryId);
    public Category updateCategory(Long categoryId, String description) throws CategoryNotFoundException;

    public Page<Category> getAllCategories(PageRequest pageRequest);

    public Optional<Category> getCategoryByIdEvenInactive(Long categoryId);
}