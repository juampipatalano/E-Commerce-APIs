package com.uade.tpo.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.demo.entity.Category;
import com.uade.tpo.demo.entity.Product;
import com.uade.tpo.demo.exceptions.CategoryDuplicateException;
import com.uade.tpo.demo.exceptions.CategoryNotFoundException;
import com.uade.tpo.demo.repository.CategoryRepository;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Category> getCategories(PageRequest pageable) {
        return categoryRepository.findAllActive(pageable);
    }

    public Optional<Category> getCategoryById(Long categoryId) {
        return categoryRepository.findActiveById(categoryId);
    }

    @Transactional(rollbackFor = Throwable.class)
    public Category createCategory(String description) throws CategoryDuplicateException {
        List<Category> categories = categoryRepository.findByDescription(description);
        if (categories.isEmpty()){
            return categoryRepository.save(new Category(description));
        }
        else{
            throw new CategoryDuplicateException();
        }
    }

    @Transactional
    @Override
    public void deleteCategory(Long categoryId) throws CategoryNotFoundException {
        Optional<Category> result = categoryRepository.findById(categoryId);
        if(result == null){
            throw new CategoryNotFoundException("La categoría " + categoryId + " no existe");
        }
        else{//Necesito la categoria en mi db debido a que está relacionado con productos, no puedo eliminarla. Se setea en false.
            Category categoria = result.get();
            categoria.setActive(false);
            categoryRepository.save(categoria);
            List<Product> productos = categoria.getProducts();
            for(Product p : productos){
                p.setActive(false);
            }
        }
    }


    @Transactional
    @Override
    public Category updateCategory(Long categoryId, String description) throws CategoryNotFoundException {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        if (categoryOpt.isEmpty()) {
            throw new CategoryNotFoundException("La categoría " + categoryId + " no existe");
        }
        Category category = categoryOpt.get();
        category.setDescription(description);
        category.setActive(true);
        return categoryRepository.save(category);

    }

}
