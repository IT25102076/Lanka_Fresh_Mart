package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.model.Order;
import com.lankafreshmart.lanka_fresh_mart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // --- CUSTOMER ENDPOINTS ---

    @GetMapping("/my-orders")
    public String myOrders(Model model, Principal principal) {
        model.addAttribute("orders", orderService.getCustomerOrders(principal.getName()));
        return "order/my-orders";
    }

    @PostMapping("/my-orders/cancel/{id}")
    public String cancelMyOrder(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id, principal.getName(), false);
            redirectAttributes.addFlashAttribute("success", "Order #" + id + " has been successfully cancelled.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error cancelling order: " + e.getMessage());
        }
        return "redirect:/orders/my-orders";
    }

    // --- ADMIN ENDPOINTS ---

    @GetMapping("/manage")
    public String manageOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "order/manage";
    }

    @PostMapping("/manage/update/{id}")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam Order.Status status, RedirectAttributes redirectAttributes) {
        try {
            orderService.updateOrderStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Order #" + id + " status updated to " + status + ".");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating order: " + e.getMessage());
        }
        return "redirect:/orders/manage";
    }

    @PostMapping("/manage/cancel/{id}")
    public String adminCancelOrder(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id, principal.getName(), true);
            redirectAttributes.addFlashAttribute("success", "Order #" + id + " was cancelled by admin. Stock restored.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error cancelling order: " + e.getMessage());
        }
        return "redirect:/orders/manage";
    }
}
