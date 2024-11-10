package com.sarthak.mycart.services.impl;

import com.sarthak.mycart.entities.Category;
import com.sarthak.mycart.exceptions.AlreadyExistsException;
import com.sarthak.mycart.exceptions.ResourceNotFoundException;
import com.sarthak.mycart.repositories.CategoryRepository;
import com.sarthak.mycart.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Category with id %d", id)));
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c -> !categoryRepository.existsByName(c.getName()))
                .map(categoryRepository :: save)
                .orElseThrow(() -> new AlreadyExistsException(String.format("Category with %s", category.getName())));
    }

    @Override
    public Category updateCategory(Category category, Long id) {
        return Optional.ofNullable(getCategoryById(id))
                .map(oldCategory -> {
                    oldCategory.setName(category.getName());
                    return oldCategory;
                }).orElseThrow(() -> new ResourceNotFoundException(String.format("Category with id %d", id)));
    }

    @Override
    public void deleteCategoryById(Long id) {
        categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository :: delete, () -> {
                    throw new ResourceNotFoundException(String.format("Category with id %d", id));
                });
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
