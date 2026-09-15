package com.lankafreshmart.lanka_fresh_mart.controller;

import com.lankafreshmart.lanka_fresh_mart.service.FinanceService;
import com.lankafreshmart.lanka_fresh_mart.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ReportService reportService;
    private final FinanceService financeService;

    @GetMapping("/executive-dashboard")
    public String viewExecutiveDashboard(Model model) {
        
        // Quick Stats
        model.addAttribute("totalCustomers", reportService.getTotalCustomers());
        model.addAttribute("totalProducts", reportService.getTotalProducts());
        model.addAttribute("lowStockCount", reportService.getLowStockProductCount());
        model.addAttribute("totalRevenue", financeService.getTotalRevenue());
        model.addAttribute("netProfit", financeService.getNetProfit());

        // Chart Data Maps
        model.addAttribute("orderCounts", reportService.getOrderStatusCounts());
        model.addAttribute("deliveryCounts", reportService.getDeliveryStatusCounts());

        return "dashboard/executive";
    }
}
