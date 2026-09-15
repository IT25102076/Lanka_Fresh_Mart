package com.lankafreshmart.lanka_fresh_mart.repository;

import com.lankafreshmart.lanka_fresh_mart.model.SupportTicket;
import com.lankafreshmart.lanka_fresh_mart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByCustomerOrderByCreatedAtDesc(User customer);
    List<SupportTicket> findAllByOrderByCreatedAtDesc();
}
