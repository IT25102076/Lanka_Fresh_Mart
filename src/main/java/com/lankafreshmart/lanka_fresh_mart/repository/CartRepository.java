package com.lankafreshmart.lanka_fresh_mart.repository;

import com.lankafreshmart.lanka_fresh_mart.model.Cart;
import com.lankafreshmart.lanka_fresh_mart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
