package com.sarthak.mycart.services.impl;

import com.sarthak.mycart.dto.ImageDto;
import com.sarthak.mycart.dto.ProductDto;
import com.sarthak.mycart.entities.Category;
import com.sarthak.mycart.entities.Image;
import com.sarthak.mycart.entities.Product;
import com.sarthak.mycart.exceptions.ResourceNotFoundException;
import com.sarthak.mycart.repositories.CategoryRepository;
import com.sarthak.mycart.repositories.ImageRepository;
import com.sarthak.mycart.repositories.ProductRepository;
import com.sarthak.mycart.request.AddProductRequest;
import com.sarthak.mycart.request.UpdateProductRequest;
import com.sarthak.mycart.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Override
    public Product addProduct(AddProductRequest request) {
        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
                .orElseGet(() -> {
                    Category newCategory = new Category(request.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        request.setCategory(category);
        return productRepository.save(createProduct(request, category));
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product with id %d", id)));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException(String.format("Product with id %d", id));
                        });
    }

    @Override
    public Product updateProductById(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingProduct -> updateProduct(request, existingProduct))
                .map(productRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Product with id %d", productId)));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String categoryName) {
        return productRepository.findByCategoryName(categoryName);
    }

    @Override
    public List<Product> getProductsByBrand(String brandName) {
        return productRepository.findByBrand(brandName);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String brandName, String categoryName) {
        return productRepository.findByCategoryNameAndBrand(categoryName, brandName);
    }

    @Override
    public List<Product> getProductsByName(String productName) {
        return productRepository.findByName(productName);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brandName, String productName) {
        return productRepository.getProductByBrandAndName(brandName, productName);
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    private Product updateProduct(UpdateProductRequest request, Product existingProduct) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory().getName());
        existingProduct.setCategory(category);
        return existingProduct;
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos = images.stream()
                .map(image -> modelMapper.map(image, ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;
    }
}
