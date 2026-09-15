package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.model.SupportTicket;
import com.lankafreshmart.lanka_fresh_mart.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/support")
@RequiredArgsConstructor
public class SupportController {

    private final SupportService supportService;

    // CUSTOMER ENDPOINTS

    @GetMapping("/my-tickets")
    public String myTickets(Model model, Principal principal) {
        model.addAttribute("tickets", supportService.getTicketsForCustomer(principal.getName()));
        return "support/my-tickets";
    }

    @PostMapping("/create")
    public String createTicket(@RequestParam String subject, @RequestParam String message, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            supportService.createTicket(principal.getName(), subject, message);
            redirectAttributes.addFlashAttribute("success", "Ticket created successfully. Our team will get back to you soon!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating ticket: " + e.getMessage());
        }
        return "redirect:/support/my-tickets";
    }

    // ADMIN / CUSTOMER RELATIONS OFFICER ENDPOINTS

    @GetMapping("/manage")
    public String manageTickets(Model model) {
        model.addAttribute("tickets", supportService.getAllTickets());
        return "support/manage";
    }

    @PostMapping("/reply/{id}")
    public String replyToTicket(@PathVariable Long id, @RequestParam String adminReply, @RequestParam SupportTicket.Status status, RedirectAttributes redirectAttributes) {
        try {
            supportService.replyToTicket(id, adminReply, status);
            redirectAttributes.addFlashAttribute("success", "Ticket updated and replied successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating ticket: " + e.getMessage());
        }
        return "redirect:/support/manage";
    }

    @PostMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            supportService.deleteTicket(id);
            redirectAttributes.addFlashAttribute("success", "Ticket deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting ticket: " + e.getMessage());
        }
        return "redirect:/support/manage";
    }
}
