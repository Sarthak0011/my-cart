package com.sarthak.mycart.services;

import com.sarthak.mycart.entities.Category;

import java.util.List;

public interface CategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    Category addCategory(Category category);
    Category updateCategory(Category category, Long id);
    void deleteCategoryById(Long id);
    List<Category> getAllCategories();
}
