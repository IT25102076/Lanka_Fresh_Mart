package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.Product;
import com.lankafreshmart.lanka_fresh_mart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryService inventoryService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findByAvailability(Product.Availability.AVAILABLE);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Transactional
    public Product saveProduct(Product product) {
        if (product.getProductCode() == null || product.getProductCode().isEmpty()) {
            product.setProductCode("PRD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        Product savedProduct = productRepository.save(product);
        inventoryService.checkAndCreateAlert(savedProduct);
        return savedProduct;
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);
        
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setUnit(updatedProduct.getUnit());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
        existingProduct.setQuantityOnHand(updatedProduct.getQuantityOnHand());
        existingProduct.setReorderLevel(updatedProduct.getReorderLevel());
        existingProduct.setAvailability(updatedProduct.getAvailability());
        
        Product savedProduct = productRepository.save(existingProduct);
        inventoryService.checkAndCreateAlert(savedProduct);
        return savedProduct;
    }

    @Transactional
    public void deleteProduct(Long id) {
        Product existingProduct = getProductById(id);
        existingProduct.setAvailability(Product.Availability.UNAVAILABLE);
        productRepository.save(existingProduct);
    }
}
