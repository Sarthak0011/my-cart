package com.sarthak.mycart.repositories;

import com.sarthak.mycart.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String categoryName);

    List<Product> findByBrand(String brandName);

    List<Product> findByCategoryNameAndBrand(String categoryName, String brandName);

    List<Product> findByName(String productName);

    List<Product> getProductByBrandAndName(String brandName, String productName);
}
