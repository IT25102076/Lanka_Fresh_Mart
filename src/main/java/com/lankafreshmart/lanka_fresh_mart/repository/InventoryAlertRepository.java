package com.lankafreshmart.lanka_fresh_mart.repository;

import com.lankafreshmart.lanka_fresh_mart.model.InventoryAlert;
import com.lankafreshmart.lanka_fresh_mart.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InventoryAlertRepository extends JpaRepository<InventoryAlert, Long> {
    Optional<InventoryAlert> findByProductAndIsResolvedFalse(Product product);
    List<InventoryAlert> findByIsResolvedFalseOrderByCreatedAtDesc();
}
