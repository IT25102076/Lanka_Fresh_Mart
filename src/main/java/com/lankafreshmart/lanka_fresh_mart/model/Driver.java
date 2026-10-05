package com.lankafreshmart.lanka_fresh_mart.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drivers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String phone;

    private String vehicleType;

    private String vehicleNumber;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private DriverStatus status = DriverStatus.AVAILABLE;

    public enum DriverStatus {
        AVAILABLE, UNAVAILABLE
    }

    @Builder.Default
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<DeliveryRoute> deliveryRoutes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<Delivery> deliveries = new ArrayList<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
