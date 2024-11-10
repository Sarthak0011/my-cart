package com.sarthak.mycart.controllers;


import com.sarthak.mycart.dto.ProductDto;
import com.sarthak.mycart.entities.Product;
import com.sarthak.mycart.exceptions.ResourceNotFoundException;
import com.sarthak.mycart.request.AddProductRequest;
import com.sarthak.mycart.request.UpdateProductRequest;
import com.sarthak.mycart.response.ApiResponse;
import com.sarthak.mycart.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getProducts() {
        try {
            logger.info("__________> Fetching all products");
            List<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                logger.info("___________> No products are available");
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse(true, "No Products are available.", products));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            logger.info("__________> Products fetched");
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Product fetched.", productDtos));
        } catch (Exception e) {
            logger.error("__________> Error fetching all products:{}", e.getMessage());
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to get products", e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            logger.info("_________> Fetching product by ID...");
            Product product = productService.getProductById(productId);
            logger.info("__________> Product fetched by ID.");
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Product found.", product));
        } catch (ResourceNotFoundException e) {
            logger.error("___________> Product not found: {}", e.getMessage());
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Product not found", e.getMessage()));
        } catch (Exception e) {
            logger.error("____________> Product not found: {}", e.getMessage());
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to get product!", e.getMessage()));
        }
    }

    @PostMapping("/product/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest) {
        try {
            logger.info("_________> Adding product...");
            Product product = productService.addProduct(productRequest);
            logger.info("_________> Product {} added.", product.getId());
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Product added.", product));
        } catch (Exception e) {
            logger.error("__________> Add product failed: {}", e.getMessage());
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to add product!", e.getMessage()));
        }
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest updateProductRequest, @PathVariable Long productId) {
        try {
            logger.info("___________> Updating product with ID: {}", productId);
            Product updatedProduct = productService.updateProductById(updateProductRequest, productId);
            logger.info("__________> Product with ID: {} updated successfully", productId);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Product updated.", updatedProduct));
        } catch (ResourceNotFoundException e) {
            logger.error("__________> Product with ID: {} not found while updating -> {}", productId, e.getMessage());
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Product not updated!", e.getMessage()));
        } catch (Exception e) {
            logger.error("__________> Failed to update product with ID: {} -> {}", productId, e.getMessage());
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to update product!", e.getMessage()));
        }
    }

    @DeleteMapping("/product/{productId}/delete")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProductById(productId);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Product deleted.", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Product not deleted!", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to delete product!", e.getMessage()));
        }
    }

    @GetMapping("/products/by-brand-and-name")
    public ResponseEntity<ApiResponse> getProductByBrandAndName(@RequestParam String brandName, @RequestParam String productName) {
        try {
            List<Product> products = productService.getProductsByBrandAndName(brandName, productName);
            if (products.isEmpty()) {
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse(false, "Products not fetched!", null));
            }
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Products fetched.", products));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to fetch products!", e.getMessage()));
        }
    }

    @GetMapping("/products/by-category-and-brand")
    public ResponseEntity<ApiResponse> getProductByCategoryAndBrand(@RequestParam String brandName, @RequestParam String categoryName) {
        try {
            List<Product> products = productService.getProductsByCategoryAndBrand(brandName, categoryName);
            if (products.isEmpty()) {
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse(true, "No products available!", null));
            }
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Products fetched.", products));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to fetch products!", e.getMessage()));
        }
    }

    @GetMapping("/products/by-name")
    public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String productName) {
        try {
            List<Product> products = productService.getProductsByName(productName);
            if (products.isEmpty()) {
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse(false, "Products not fetched!", null));
            }
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Products fetched.", products));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to fetch products!", e.getMessage()));
        }
    }

    @GetMapping("/products/by-brand")
    public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brandName) {
        try {
            List<Product> products = productService.getProductsByBrand(brandName);
            if (products.isEmpty()) {
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse(false, "Products not fetched!", null));
            }
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Products fetched.", products));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to fetch products!", e.getMessage()));
        }
    }

    @GetMapping("/products/by-category")
    public ResponseEntity<ApiResponse> getProductsByCategory(@RequestParam String categoryName) {
        try {
            List<Product> products = productService.getProductsByCategory(categoryName);
            if (products.isEmpty()) {
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse(false, "Products not fetched!", null));
            }
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Products fetched.", products));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to fetch products!", e.getMessage()));
        }
    }

}
