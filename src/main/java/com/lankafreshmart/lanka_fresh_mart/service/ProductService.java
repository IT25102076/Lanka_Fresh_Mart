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
    private final com.lankafreshmart.lanka_fresh_mart.repository.InventoryAlertRepository inventoryAlertRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getAvailableProducts() {
        return productRepository.findByAvailability(Product.Availability.AVAILABLE);
    }

    public List<Product> getProductsByCategory(Product.Category category) {
        return productRepository.findByCategory(category).stream()
                .filter(p -> p.getAvailability() == Product.Availability.AVAILABLE)
                .toList();
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query).stream()
                .filter(p -> p.getAvailability() == Product.Availability.AVAILABLE)
                .toList();
    }

    public Product getMostSellingProduct() {
        return productRepository.findMostSellingProduct().orElseGet(() -> {
            List<Product> available = getAvailableProducts();
            return available.isEmpty() ? null : available.get(0);
        });
    }

    public List<Product> getMostSellingProducts(int limit) {
        List<Product> products = new java.util.ArrayList<>(productRepository.findTopSellingProducts(org.springframework.data.domain.PageRequest.of(0, limit)));
        if (products.size() < limit) {
            List<Product> available = getAvailableProducts();
            for (Product p : available) {
                if (products.size() >= limit) break;
                if (!products.contains(p)) products.add(p);
            }
        }
        return products;
    }

    public List<Product> getWeeklyBestSellingByCategory(Product.Category category, int limit) {
        java.time.LocalDateTime startDate = java.time.LocalDateTime.now().minusDays(7);
        List<Product> products = new java.util.ArrayList<>(productRepository.findWeeklyTopSellingProductsByCategory(
                category, startDate, org.springframework.data.domain.PageRequest.of(0, limit)
        ));
        if (products.size() < limit) {
            List<Product> available = getProductsByCategory(category);
            for (Product p : available) {
                if (products.size() >= limit) break;
                if (!products.contains(p)) products.add(p);
            }
        }
        return products;
    }

    public java.util.Map<String, List<Product>> getWeeklyBestSellingByCategoryMap(int limit) {
        java.util.Map<String, List<Product>> map = new java.util.LinkedHashMap<>();
        map.put("Fresh Vegetables", getWeeklyBestSellingByCategory(Product.Category.VEGETABLES, limit));
        map.put("Fruits", getWeeklyBestSellingByCategory(Product.Category.FRUITS, limit));
        map.put("Dairy & Eggs", getWeeklyBestSellingByCategory(Product.Category.DAIRY, limit));
        map.put("Bakery", getWeeklyBestSellingByCategory(Product.Category.BAKERY, limit));
        map.put("Meat & Fish", getWeeklyBestSellingByCategory(Product.Category.MEAT, limit));
        map.put("Beverages", getWeeklyBestSellingByCategory(Product.Category.BEVERAGES, limit));
        return map;
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
        try {
            productRepository.deleteById(id);
            productRepository.flush(); // Force the delete to catch constraints
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Product is used in orders, soft delete instead
            Product existingProduct = getProductById(id);
            existingProduct.setAvailability(Product.Availability.UNAVAILABLE);
            productRepository.save(existingProduct);
        }
    }

    @Transactional
    public void hardDeleteProduct(Long id) {
        Product product = getProductById(id);
        try {
            inventoryAlertRepository.deleteByProduct(product);
            productRepository.deleteById(id);
            productRepository.flush(); // Force immediate execution to catch foreign key constraints
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Fallback to soft delete if it's already used in Orders
            product.setAvailability(Product.Availability.UNAVAILABLE);
            productRepository.save(product);
            throw new RuntimeException("Product is linked to existing data (like Orders or Carts). It has been safely Discontinued instead of permanently deleted.");
        }
    }
}
