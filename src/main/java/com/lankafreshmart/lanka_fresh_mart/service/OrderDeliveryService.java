package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.*;
import com.lankafreshmart.lanka_fresh_mart.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDeliveryService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final DeliveryRepository deliveryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryService inventoryService;

    @Transactional
    public Delivery createOrderFromCart(String username, String deliveryAddress) {
        Cart cart = cartService.getCartForUser(username);
        
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Calculate total and prepare order
        BigDecimal total = BigDecimal.ZERO;
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(Order.Status.PENDING); // Initially pending so it can be cancelled

        // Process items and reduce stock
        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();
            if (product.getQuantityOnHand() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            
            // Reduce stock
            product.setQuantityOnHand(product.getQuantityOnHand() - cartItem.getQuantity());
            productRepository.save(product);
            
            // Check for low stock alerts
            inventoryService.checkAndCreateAlert(product);

            // Create Order Item
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtTime(cartItem.getPriceAtTime());
            order.getItems().add(orderItem);

            BigDecimal itemTotal = cartItem.getPriceAtTime().multiply(new BigDecimal(cartItem.getQuantity()));
            total = total.add(itemTotal);
        }

        order.setTotalAmount(total);
        order = orderRepository.save(order);

        // Clear the cart
        cartService.clearCart(username);

        // Create Delivery record
        Delivery delivery = new Delivery();
        delivery.setOrder(order);
        delivery.setDeliveryAddress(deliveryAddress);
        delivery.setStatus(Delivery.Status.PREPARING);
        delivery.setScheduledDate(LocalDateTime.now().plusDays(1)); // Schedule for next day
        
        return deliveryRepository.save(delivery);
    }

    public List<Delivery> getUserDeliveries(String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return deliveryRepository.findByOrderUserOrderByCreatedAtDesc(user);
    }

    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    @Transactional
    public void updateDeliveryStatus(Long deliveryId, Delivery.Status newStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found"));
        delivery.setStatus(newStatus);
        deliveryRepository.save(delivery);
    }
}
