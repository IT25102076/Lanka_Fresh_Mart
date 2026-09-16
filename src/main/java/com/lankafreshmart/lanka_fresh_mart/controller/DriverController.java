package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.model.Driver;
import com.lankafreshmart.lanka_fresh_mart.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping
    public String listDrivers(Model model) {
        model.addAttribute("drivers", driverService.getAllDrivers());
        return "driver/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("driver", new Driver());
        return "driver/form";
    }

    @PostMapping("/create")
    public String saveDriver(@ModelAttribute Driver driver, RedirectAttributes redirectAttributes) {
        try {
            driverService.saveDriver(driver);
            redirectAttributes.addFlashAttribute("success", "Driver successfully saved.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving driver: " + e.getMessage());
        }
        return "redirect:/drivers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("driver", driverService.getDriverById(id));
        return "driver/form";
    }

    @PostMapping("/edit/{id}")
    public String updateDriver(@PathVariable Long id, @ModelAttribute Driver driver, RedirectAttributes redirectAttributes) {
        try {
            Driver existing = driverService.getDriverById(id);
            existing.setName(driver.getName());
            existing.setPhone(driver.getPhone());
            existing.setVehicleType(driver.getVehicleType());
            driverService.saveDriver(existing);
            redirectAttributes.addFlashAttribute("success", "Driver updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating driver: " + e.getMessage());
        }
        return "redirect:/drivers";
    }

    @PostMapping("/delete/{id}")
    public String deleteDriver(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            driverService.deleteDriver(id);
            redirectAttributes.addFlashAttribute("success", "Driver deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/drivers";
    }
}
