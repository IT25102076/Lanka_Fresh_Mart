package com.lankafreshmart.lanka_fresh_mart.repository;

import com.lankafreshmart.lanka_fresh_mart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductCode(String productCode);
    List<Product> findByAvailability(Product.Availability availability);
    List<Product> findByCategory(Product.Category category);
}
