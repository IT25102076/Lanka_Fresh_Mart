package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.service.FinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/dashboard")
    public String viewDashboard(Model model) {
        BigDecimal revenue = financeService.getTotalRevenue();
        BigDecimal expenses = financeService.getTotalExpenses();
        BigDecimal profit = financeService.getNetProfit();
        
        model.addAttribute("totalRevenue", revenue);
        model.addAttribute("totalExpenses", expenses);
        model.addAttribute("netProfit", profit);
        model.addAttribute("expensesList", financeService.getAllExpenses());
        
        return "finance/dashboard";
    }

    @PostMapping("/expenses/add")
    public String addExpense(@RequestParam String description, @RequestParam BigDecimal amount, RedirectAttributes redirectAttributes) {
        try {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero.");
            }
            financeService.addExpense(description, amount);
            redirectAttributes.addFlashAttribute("success", "Expense logged successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add expense: " + e.getMessage());
        }
        return "redirect:/finance/dashboard";
    }
}
