package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.Order;
import com.lankafreshmart.lanka_fresh_mart.model.OrderItem;
import com.lankafreshmart.lanka_fresh_mart.model.Product;
import com.lankafreshmart.lanka_fresh_mart.model.User;
import com.lankafreshmart.lanka_fresh_mart.repository.OrderRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.ProductRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RefundService refundService;
    private final com.lankafreshmart.lanka_fresh_mart.repository.DeliveryRepository deliveryRepository;
    private final com.lankafreshmart.lanka_fresh_mart.repository.RefundRepository refundRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public List<Order> getCustomerOrders(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    public void updateOrderStatus(Long orderId, Order.Status status) {
        Order order = getOrderById(orderId);
        order.setStatus(status);
        orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId, String requestedByEmail, boolean isAdmin) {
        Order order = getOrderById(orderId);
        
        // Security check: Only admins or the order owner can cancel
        if (!isAdmin && !order.getUser().getEmail().equals(requestedByEmail)) {
            throw new RuntimeException("You are not authorized to cancel this order.");
        }
        
        // Business logic: Customers can only cancel PENDING orders
        if (!isAdmin && order.getStatus() != Order.Status.PENDING) {
            throw new RuntimeException("Order cannot be cancelled because it is already " + order.getStatus() + ".");
        }

        if (order.getStatus() == Order.Status.CANCELLED) {
            throw new RuntimeException("Order is already cancelled.");
        }

        // Restore stock for all items in the order
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setQuantityOnHand(product.getQuantityOnHand() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(Order.Status.CANCELLED);
        orderRepository.save(order);
        
        // Cancel the associated delivery if it exists
        deliveryRepository.findByOrderId(orderId).ifPresent(delivery -> {
            delivery.setStatus(com.lankafreshmart.lanka_fresh_mart.model.Delivery.Status.CANCELLED);
            deliveryRepository.save(delivery);
        });
        
        // Automatically create a pending refund for the cancelled order
        refundService.createRefundForOrder(order);
    }

    @Transactional
    public void hardDeleteOrder(Long orderId) {
        Order order = getOrderById(orderId);
        if (order.getStatus() != Order.Status.CANCELLED && order.getStatus() != Order.Status.REFUNDED) {
            throw new RuntimeException("Only cancelled or refunded orders can be permanently deleted.");
        }
        
        // Remove associated delivery if exists to prevent foreign key errors
        deliveryRepository.findByOrderId(orderId).ifPresent(delivery -> {
            deliveryRepository.delete(delivery);
        });

        // Check if there is a pending refund, block deletion if so
        refundRepository.findByOrderId(orderId).ifPresent(refund -> {
            if (refund.getStatus() == com.lankafreshmart.lanka_fresh_mart.model.Refund.Status.PENDING) {
                throw new RuntimeException("You must process the pending refund for this order before it can be deleted.");
            }
            refundRepository.delete(refund);
        });

        orderRepository.delete(order);
    }
}
