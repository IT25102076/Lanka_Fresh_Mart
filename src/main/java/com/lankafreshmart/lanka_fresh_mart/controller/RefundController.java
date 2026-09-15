package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/finance/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @GetMapping
    public String viewRefunds(Model model) {
        model.addAttribute("refunds", refundService.getAllRefunds());
        return "finance/refunds";
    }

    @PostMapping("/process/{id}")
    public String processRefund(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            refundService.processRefund(id);
            redirectAttributes.addFlashAttribute("success", "Refund #" + id + " has been successfully processed.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error processing refund: " + e.getMessage());
        }
        return "redirect:/finance/refunds";
    }
}
