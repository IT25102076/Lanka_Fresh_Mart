package com.lankafreshmart.lanka_fresh_mart.repository;

import com.lankafreshmart.lanka_fresh_mart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCode(String productCode);
    List<Product> findByAvailability(Product.Availability availability);
    List<Product> findByCategory(Product.Category category);
    List<Product> findByNameContainingIgnoreCase(String name);

    @org.springframework.data.jpa.repository.Query(value = "SELECT p.* FROM products p JOIN order_items oi ON p.id = oi.product_id GROUP BY p.id ORDER BY SUM(oi.quantity) DESC LIMIT 1", nativeQuery = true)
    Optional<Product> findMostSellingProduct();

    @org.springframework.data.jpa.repository.Query("SELECT oi.product FROM OrderItem oi WHERE oi.product.availability = 'AVAILABLE' GROUP BY oi.product ORDER BY SUM(oi.quantity) DESC")
    List<Product> findTopSellingProducts(org.springframework.data.domain.Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT oi.product FROM OrderItem oi JOIN oi.order o WHERE oi.product.availability = 'AVAILABLE' AND o.createdAt >= :startDate GROUP BY oi.product ORDER BY SUM(oi.quantity) DESC")
    List<Product> findWeeklyTopSellingProducts(@org.springframework.data.repository.query.Param("startDate") java.time.LocalDateTime startDate, org.springframework.data.domain.Pageable pageable);

    @org.springframework.data.jpa.repository.Query("SELECT oi.product FROM OrderItem oi JOIN oi.order o WHERE oi.product.availability = 'AVAILABLE' AND oi.product.category = :category AND o.createdAt >= :startDate GROUP BY oi.product ORDER BY SUM(oi.quantity) DESC")
    List<Product> findWeeklyTopSellingProductsByCategory(@org.springframework.data.repository.query.Param("category") Product.Category category, @org.springframework.data.repository.query.Param("startDate") java.time.LocalDateTime startDate, org.springframework.data.domain.Pageable pageable);
}
