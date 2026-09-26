package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.model.Delivery;
import com.lankafreshmart.lanka_fresh_mart.service.OrderDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.lankafreshmart.lanka_fresh_mart.service.CartService;
import com.lankafreshmart.lanka_fresh_mart.service.StripeService;

import com.lankafreshmart.lanka_fresh_mart.service.DriverService;
import com.lankafreshmart.lanka_fresh_mart.repository.RefundRepository;
import com.lankafreshmart.lanka_fresh_mart.model.Refund;

@Controller
@RequiredArgsConstructor
public class DeliveryController {

    private final OrderDeliveryService deliveryService;
    private final CartService cartService;
    private final StripeService stripeService;
    private final DriverService driverService;
    private final RefundRepository refundRepository;

    @GetMapping("/checkout")
    public String viewCheckoutPage(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        try {
            model.addAttribute("cart", cartService.getCartForUser(authentication.getName()));
        } catch (Exception e) {
            // Cart may be empty or user not found, proceed anyway
        }
        return "delivery/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam String deliveryAddress, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            com.lankafreshmart.lanka_fresh_mart.model.Cart cart = cartService.getCartForUser(authentication.getName());

            if (cart.getItems().isEmpty()) {
                throw new RuntimeException("Cart is empty");
            }

            com.stripe.model.checkout.Session session = stripeService.createCheckoutSession(cart, deliveryAddress);
            return "redirect:" + session.getUrl();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Payment Initialization Failed: " + e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/checkout/success")
    public String checkoutSuccess(@RequestParam("session_id") String sessionId, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            com.stripe.model.checkout.Session session = stripeService.retrieveSession(sessionId);
            
            if ("paid".equals(session.getPaymentStatus())) {
                String deliveryAddress = session.getMetadata().get("deliveryAddress");
                Delivery delivery = deliveryService.createOrderFromCart(authentication.getName(), deliveryAddress);
                redirectAttributes.addFlashAttribute("success", "Payment successful! Order placed. Track your delivery here.");
                return "redirect:/deliveries/track?orderId=" + delivery.getOrder().getId();
            } else {
                redirectAttributes.addFlashAttribute("error", "Payment not completed.");
                return "redirect:/cart";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error verifying payment: " + e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/checkout/cancel")
    public String checkoutCancel(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Payment was cancelled. You can try again when ready.");
        return "redirect:/cart";
    }

    @GetMapping("/deliveries/track")
    public String trackDeliveries(@RequestParam(required = false) Long orderId, Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        if (orderId != null) {
            Delivery delivery = deliveryService.getDeliveryForTracking(orderId, authentication.getName());
            if (delivery != null) {
                model.addAttribute("delivery", delivery);
                
                // For cancelled orders, look up refund status from the refunds table
                if (delivery.getOrder().getStatus() == com.lankafreshmart.lanka_fresh_mart.model.Order.Status.CANCELLED) {
                    Refund refund = refundRepository.findByOrderId(orderId).orElse(null);
                    model.addAttribute("refund", refund);
                }
            } else {
                model.addAttribute("error", "Invalid Order ID or you don't have permission to view it.");
            }
        }
        
        return "delivery/track";
    }

    @GetMapping("/deliveries/manage")
    public String manageDeliveries(Model model) {
        model.addAttribute("deliveries", deliveryService.getAllDeliveries());
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "delivery/manage";
    }

    @PostMapping("/deliveries/update/{id}")
    public String updateDeliveryStatus(@PathVariable Long id, @RequestParam Delivery.Status status, 
                                       @RequestParam(required = false) Long driverId,
                                       RedirectAttributes redirectAttributes) {
        try {
            deliveryService.updateDeliveryStatus(id, status, driverId);
            redirectAttributes.addFlashAttribute("success", "Delivery status updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/deliveries/manage";
    }
}
