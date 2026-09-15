package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.model.Delivery;
import com.lankafreshmart.lanka_fresh_mart.service.OrderDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class DeliveryController {

    private final OrderDeliveryService deliveryService;

    @GetMapping("/checkout")
    public String viewCheckoutPage(Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        return "delivery/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam String deliveryAddress, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            Delivery delivery = deliveryService.createOrderFromCart(authentication.getName(), deliveryAddress);
            redirectAttributes.addFlashAttribute("success", "Order placed successfully! Track your delivery here.");
            return "redirect:/deliveries/track";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/deliveries/track")
    public String trackDeliveries(Model model, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        model.addAttribute("deliveries", deliveryService.getUserDeliveries(authentication.getName()));
        return "delivery/track";
    }

    @GetMapping("/deliveries/manage")
    public String manageDeliveries(Model model) {
        model.addAttribute("deliveries", deliveryService.getAllDeliveries());
        return "delivery/manage";
    }

    @PostMapping("/deliveries/update/{id}")
    public String updateDeliveryStatus(@PathVariable Long id, @RequestParam Delivery.Status status, 
                                       @RequestParam(required = false) String driverName, 
                                       RedirectAttributes redirectAttributes) {
        try {
            deliveryService.updateDeliveryStatus(id, status, driverName);
            redirectAttributes.addFlashAttribute("success", "Delivery status updated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/deliveries/manage";
    }
}
