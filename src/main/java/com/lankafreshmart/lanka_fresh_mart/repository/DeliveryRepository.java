package com.lankafreshmart.lanka_fresh_mart.repository;

import com.lankafreshmart.lanka_fresh_mart.model.Delivery;
import com.lankafreshmart.lanka_fresh_mart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    List<Delivery> findByOrderUserOrderByCreatedAtDesc(User user);
    Optional<Delivery> findByOrderId(Long orderId);
}
