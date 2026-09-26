package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.Order;
import com.lankafreshmart.lanka_fresh_mart.model.Refund;
import com.lankafreshmart.lanka_fresh_mart.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final com.lankafreshmart.lanka_fresh_mart.repository.OrderRepository orderRepository;

    @Transactional
    public void createRefundForOrder(Order order) {
        // Guard: only create a refund if one doesn't already exist for this order
        if (refundRepository.findByOrderId(order.getId()).isPresent()) {
            return; // Refund already exists, do nothing
        }
        Refund refund = new Refund();
        refund.setOrder(order);
        refund.setAmount(order.getTotalAmount());
        refund.setStatus(Refund.Status.PENDING);
        refundRepository.save(refund);
    }

    public List<Refund> getAllRefunds() {
        return refundRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public void processRefund(Long refundId) {
        Refund refund = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund not found with ID: " + refundId));
        
        if (refund.getStatus() == Refund.Status.COMPLETED) {
            throw new RuntimeException("Refund is already completed.");
        }
        
        refund.setStatus(Refund.Status.COMPLETED);
        refund.setProcessedAt(LocalDateTime.now());
        
        Order order = refund.getOrder();
        order.setStatus(Order.Status.REFUNDED);
        orderRepository.save(order);
        
        refundRepository.save(refund);
    }
}
