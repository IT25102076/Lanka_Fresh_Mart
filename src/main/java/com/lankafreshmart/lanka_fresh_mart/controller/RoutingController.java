package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.service.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/routing")
@RequiredArgsConstructor
public class RoutingController {

    private final RoutingService routingService;

    @GetMapping("/dashboard")
    public String viewDashboard(@RequestParam(required = false) String date, Model model) {
        LocalDate selectedDate = date != null ? LocalDate.parse(date) : LocalDate.now().plusDays(1);
        
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("routes", routingService.getRoutesForDate(selectedDate));
        
        return "delivery/routing-dashboard";
    }

    @PostMapping("/optimize")
    public String optimizeRoutes(@RequestParam String date, RedirectAttributes redirectAttributes) {
        try {
            LocalDate selectedDate = LocalDate.parse(date);
            routingService.optimizeRoutesForDate(selectedDate);
            redirectAttributes.addFlashAttribute("success", "Routes have been successfully optimized and generated!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to optimize routes: " + e.getMessage());
        }
        return "redirect:/routing/dashboard?date=" + date;
    }

    @PostMapping("/assign-driver/{routeId}")
    public String assignDriver(@PathVariable Long routeId, @RequestParam String driverName, @RequestParam String date, RedirectAttributes redirectAttributes) {
        if (driverName == null || driverName.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Driver name cannot be empty.");
            return "redirect:/routing/dashboard?date=" + date;
        }

        try {
            routingService.assignDriver(routeId, driverName);
            redirectAttributes.addFlashAttribute("success", "Driver successfully assigned to route!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/routing/dashboard?date=" + date;
    }
}
