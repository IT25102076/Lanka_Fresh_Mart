package com.lankafreshmart.lanka_fresh_mart.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String productCode;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private String imageUrl;

    @Column(nullable = false)
    private Integer quantityOnHand;

    @Column(nullable = false)
    private Integer reorderLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Availability availability;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (this.availability == null) {
            this.availability = Availability.AVAILABLE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Category {
        VEGETABLES,
        FRUITS,
        MEAT,
        SEAFOOD,
        DAIRY,
        BAKERY,
        BEVERAGES,
        SNACKS,
        PERSONAL_CARE,
        HOUSEHOLD
    }

    public enum Availability {
        AVAILABLE,
        UNAVAILABLE
    }
}
