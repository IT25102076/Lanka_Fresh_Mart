package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.InventoryAlert;
import com.lankafreshmart.lanka_fresh_mart.model.Product;
import com.lankafreshmart.lanka_fresh_mart.repository.InventoryAlertRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryAlertRepository alertRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void checkAndCreateAlert(Product product) {
        if (product.getQuantityOnHand() <= product.getReorderLevel()) {
            Optional<InventoryAlert> existingAlert = alertRepository.findByProductAndIsResolvedFalse(product);
            
            if (existingAlert.isEmpty()) {
                InventoryAlert alert = new InventoryAlert();
                alert.setProduct(product);
                alert.setAlertMessage("Low stock alert! Only " + product.getQuantityOnHand() + " remaining (Reorder level: " + product.getReorderLevel() + ").");
                alertRepository.save(alert);
            }
        }
    }

    public List<InventoryAlert> getActiveAlerts() {
        return alertRepository.findByIsResolvedFalseOrderByCreatedAtDesc();
    }

    @Transactional
    public void resolveAlertAndRestock(Long alertId, int addedQuantity) {
        InventoryAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
                
        Product product = alert.getProduct();
        
        // Add new stock
        product.setQuantityOnHand(product.getQuantityOnHand() + addedQuantity);
        productRepository.save(product);
        
        // Resolve alert
        alert.setResolved(true);
        alert.setResolvedAt(LocalDateTime.now());
        alertRepository.save(alert);
    }

    @Transactional
    public void quickRestock(Long productId, int addedQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Add new stock
        product.setQuantityOnHand(product.getQuantityOnHand() + addedQuantity);
        productRepository.save(product);
        
        // Resolve any active alerts for this product
        Optional<InventoryAlert> activeAlert = alertRepository.findByProductAndIsResolvedFalse(product);
        activeAlert.ifPresent(alert -> {
            alert.setResolved(true);
            alert.setResolvedAt(LocalDateTime.now());
            alertRepository.save(alert);
        });
    }

    @Transactional
    public void createManualAlert(Long productId, String message) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
                
        InventoryAlert alert = new InventoryAlert();
        alert.setProduct(product);
        alert.setAlertMessage(message);
        alertRepository.save(alert);
    }

    @Transactional
    public void dismissAlert(Long alertId) {
        InventoryAlert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));
        alertRepository.delete(alert);
    }
}
