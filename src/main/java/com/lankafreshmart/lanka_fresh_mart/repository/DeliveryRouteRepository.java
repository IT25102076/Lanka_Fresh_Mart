package com.lankafreshmart.lanka_fresh_mart.repository;

import com.lankafreshmart.lanka_fresh_mart.model.DeliveryRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DeliveryRouteRepository extends JpaRepository<DeliveryRoute, Long> {
    List<DeliveryRoute> findByScheduledDateOrderByCreatedAtDesc(LocalDate date);
}
