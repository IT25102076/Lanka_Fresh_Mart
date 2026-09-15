package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.Delivery;
import com.lankafreshmart.lanka_fresh_mart.model.Order;
import com.lankafreshmart.lanka_fresh_mart.model.Product;
import com.lankafreshmart.lanka_fresh_mart.model.User;
import com.lankafreshmart.lanka_fresh_mart.repository.DeliveryRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.OrderRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.ProductRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;

    public long getTotalCustomers() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRole() == User.Role.CUSTOMER)
                .count();
    }

    public long getTotalProducts() {
        return productRepository.count();
    }

    public long getLowStockProductCount() {
        return productRepository.findAll().stream()
                .filter(p -> p.getQuantityOnHand() <= p.getReorderLevel())
                .count();
    }

    public Map<String, Long> getOrderStatusCounts() {
        List<Order> orders = orderRepository.findAll();
        Map<String, Long> counts = new HashMap<>();
        
        counts.put("PENDING", orders.stream().filter(o -> o.getStatus() == Order.Status.PENDING).count());
        counts.put("CONFIRMED", orders.stream().filter(o -> o.getStatus() == Order.Status.CONFIRMED).count());
        counts.put("CANCELLED", orders.stream().filter(o -> o.getStatus() == Order.Status.CANCELLED).count());
        
        return counts;
    }

    public Map<String, Long> getDeliveryStatusCounts() {
        List<Delivery> deliveries = deliveryRepository.findAll();
        Map<String, Long> counts = new HashMap<>();
        
        counts.put("PREPARING", deliveries.stream().filter(d -> d.getStatus() == Delivery.Status.PREPARING).count());
        counts.put("DISPATCHED", deliveries.stream().filter(d -> d.getStatus() == Delivery.Status.DISPATCHED).count());
        counts.put("DELIVERED", deliveries.stream().filter(d -> d.getStatus() == Delivery.Status.DELIVERED).count());
        
        return counts;
    }
}
