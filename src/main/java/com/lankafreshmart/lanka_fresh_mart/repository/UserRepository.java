package com.lankafreshmart.lanka_fresh_mart.repository;

import com.lankafreshmart.lanka_fresh_mart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
