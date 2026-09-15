package com.lankafreshmart.lanka_fresh_mart.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_alerts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InventoryAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String alertMessage;

    @Column(nullable = false)
    private boolean isResolved;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime resolvedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (!isResolved) {
            isResolved = false;
        }
    }
}
