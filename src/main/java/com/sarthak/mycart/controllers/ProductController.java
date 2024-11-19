package com.sarthak.mycart.controllers;


import com.sarthak.mycart.dto.ProductDto;
import com.sarthak.mycart.entities.Product;
import com.sarthak.mycart.exceptions.ResourceNotFoundException;
import com.sarthak.mycart.request.AddProductRequest;
import com.sarthak.mycart.request.UpdateProductRequest;
import com.sarthak.mycart.response.ApiResponse;
import com.sarthak.mycart.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    
    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getProducts() {
        try {
            log.info("__________> Fetching all products");
            List<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                log.info("___________> No products are available");
                return ResponseEntity
                        .status(NOT_FOUND)
                        .body(new ApiResponse(true, "No Products are available.", products));
            }
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            log.info("__________> Products fetched");
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Product fetched.", productDtos));
        } catch (Exception e) {
            log.error("__________> Error fetching all products:{}", e.getMessage());
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to get products", e.getMessage()));
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
        try {
            log.info("_________> Fetching product by ID...");
            Product product = productService.getProductById(productId);
            ProductDto productDto = productService.convertToDto(product);
            log.info("__________> Product fetched by ID.");
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Product found.", productDto));
        } catch (ResourceNotFoundException e) {
            log.error("___________> Product not found: {}", e.getMessage());
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Product not found", e.getMessage()));
        } catch (Exception e) {
            log.error("____________> Product not found: {}", e.getMessage());
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to get product!", e.getMessage()));
        }
    }

    @PostMapping("/product/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest) {
        try {
            log.info("_________> Adding product...");
            Product product = productService.addProduct(productRequest);
            ProductDto productDto = productService.convertToDto(product);
            log.info("_________> Product {} added.", product.getId());
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Product added.", productDto));
        } catch (Exception e) {
            log.error("__________> Add product failed: {}", e.getMessage());
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to add product!", e.getMessage()));
        }
    }

    @PutMapping("/product/{productId}/update")
    public ResponseEntity<ApiResponse> updateProduct(@RequestBody UpdateProductRequest updateProductRequest, @PathVariable Long productId) {
        try {
            log.info("___________> Updating product with ID: {}", productId);
            Product updatedProduct = productService.updateProductById(updateProductRequest, productId);
            ProductDto productDto = productService.convertToDto(updatedProduct);
            log.info("__________> Product with ID: {} updated successfully", productDto);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Product updated.", updatedProduct));
        } catch (ResourceNotFoundException e) {
            log.error("__________> Product with ID: {} not found while updating -> {}", productId, e.getMessage());
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(new ApiResponse(false, "Product not updated!", e.getMessage()));
        } catch (Exception e) {
            log.error("__________> Failed to update product with ID: {} -> {}", productId, e.getMessage());
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
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Products fetched.", productDtos));
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
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Products fetched.", productDtos));
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
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Products fetched.", productDtos));
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
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Products fetched.", productDtos));
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
            List<ProductDto> productDtos = productService.getConvertedProducts(products);
            return ResponseEntity
                    .status(OK)
                    .body(new ApiResponse(true, "Products fetched.", productDtos));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to fetch products!", e.getMessage()));
        }
    }
}
