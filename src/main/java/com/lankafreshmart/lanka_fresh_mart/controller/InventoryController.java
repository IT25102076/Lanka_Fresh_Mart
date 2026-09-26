package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    private final com.lankafreshmart.lanka_fresh_mart.service.ProductService productService;

    @GetMapping("/alerts")
    public String viewAlerts(Model model) {
        java.util.List<com.lankafreshmart.lanka_fresh_mart.model.Product> allProducts = productService.getAllProducts();
        java.util.List<com.lankafreshmart.lanka_fresh_mart.model.Product> lowStockProducts = allProducts.stream()
                .filter(p -> p.getQuantityOnHand() <= p.getReorderLevel())
                .collect(java.util.stream.Collectors.toList());
        
        long outOfStockCount = lowStockProducts.stream().filter(p -> p.getQuantityOnHand() == 0).count();
        long lowStockCount = lowStockProducts.stream().filter(p -> p.getQuantityOnHand() > 0).count();

        model.addAttribute("alerts", inventoryService.getActiveAlerts());
        model.addAttribute("products", allProducts);
        model.addAttribute("lowStockProducts", lowStockProducts);
        model.addAttribute("outOfStockCount", outOfStockCount);
        model.addAttribute("lowStockCount", lowStockCount);
        
        return "inventory/alerts";
    }

    @PostMapping("/restock/{alertId}")
    public String restockProduct(@PathVariable Long alertId, @RequestParam int restockAmount, RedirectAttributes redirectAttributes) {
        if (restockAmount <= 0) {
            redirectAttributes.addFlashAttribute("error", "Restock amount must be greater than zero.");
            return "redirect:/inventory/alerts";
        }
        
        try {
            inventoryService.resolveAlertAndRestock(alertId, restockAmount);
            redirectAttributes.addFlashAttribute("success", "Product successfully restocked and alert resolved!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/inventory/alerts";
    }

    @PostMapping("/quick-restock/{productId}")
    public String quickRestockProduct(@PathVariable Long productId, @RequestParam int restockAmount, RedirectAttributes redirectAttributes) {
        if (restockAmount <= 0) {
            redirectAttributes.addFlashAttribute("error", "Restock amount must be greater than zero.");
            return "redirect:/inventory/alerts";
        }
        
        try {
            inventoryService.quickRestock(productId, restockAmount);
            redirectAttributes.addFlashAttribute("success", "Product successfully restocked!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/inventory/alerts";
    }

    @PostMapping("/alerts/create")
    public String createManualAlert(@RequestParam Long productId, @RequestParam String message, RedirectAttributes redirectAttributes) {
        try {
            inventoryService.createManualAlert(productId, message);
            redirectAttributes.addFlashAttribute("success", "Manual inventory alert created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create alert: " + e.getMessage());
        }
        return "redirect:/inventory/alerts";
    }

    @PostMapping("/alerts/dismiss/{alertId}")
    public String dismissAlert(@PathVariable Long alertId, RedirectAttributes redirectAttributes) {
        try {
            inventoryService.dismissAlert(alertId);
            redirectAttributes.addFlashAttribute("success", "Alert successfully dismissed and deleted from the database.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to dismiss alert: " + e.getMessage());
        }
        return "redirect:/inventory/alerts";
    }
}
