package com.sarthak.mycart.repositories;

import com.sarthak.mycart.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String categoryName);

    List<Product> findByBrandName(String brandName);

    List<Product> findByCategoryAndBrand(String brandName, String categoryName);

    List<Product> findByProductName(String productName);
}
