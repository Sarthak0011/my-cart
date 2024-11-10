package com.sarthak.mycart.services;

import com.sarthak.mycart.dto.ProductDto;
import com.sarthak.mycart.entities.Product;
import com.sarthak.mycart.request.AddProductRequest;
import com.sarthak.mycart.request.UpdateProductRequest;

import java.util.List;

public interface ProductService {
    Product addProduct(AddProductRequest product);

    Product getProductById(Long id);

    void deleteProductById(Long id);

    Product updateProductById(UpdateProductRequest product, Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String categoryName);

    List<Product> getProductsByBrand(String brandName);

    List<Product> getProductsByCategoryAndBrand(String brandName, String categoryName);

    List<Product> getProductsByName(String productName);

    List<Product> getProductsByBrandAndName(String brandName, String productName);

    List<ProductDto> getConvertedProducts(List<Product> products);

    ProductDto convertToDto(Product product);
}
