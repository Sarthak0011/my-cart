package com.sarthak.mycart.controllers;

import com.sarthak.mycart.entities.Category;
import com.sarthak.mycart.exceptions.AlreadyExistsException;
import com.sarthak.mycart.exceptions.ResourceNotFoundException;
import com.sarthak.mycart.response.ApiResponse;
import com.sarthak.mycart.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Categories fetched.", categories));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to fetch categories", e.getMessage()));
        }
    }

    @PostMapping("/category/add")
    public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
        try {
            Category savedCategory = categoryService.addCategory(category);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Category saved.", savedCategory));
        } catch (AlreadyExistsException e) {
            return ResponseEntity
                    .status(CONFLICT)
                    .body(new ApiResponse(false, "Category Exists!", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to save category!", e.getMessage()));
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Category found.", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Category not found!", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to get category", e.getMessage()));
        }
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<ApiResponse> getCategoryByName(@PathVariable String categoryName) {
        try {
            Category category = categoryService.getCategoryByName(categoryName);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Category found.", category));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Category not found!", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to get category", e.getMessage()));
        }
    }

    @DeleteMapping("/category/{categoryId}/delete")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {
        try {
            categoryService.deleteCategoryById(categoryId);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Category found.", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Category not found!", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to delete category", e.getMessage()));
        }
    }


}
