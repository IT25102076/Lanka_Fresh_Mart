package com.lankafreshmart.lanka_fresh_mart.service;

import com.lankafreshmart.lanka_fresh_mart.model.Expense;
import com.lankafreshmart.lanka_fresh_mart.model.Order;
import com.lankafreshmart.lanka_fresh_mart.repository.ExpenseRepository;
import com.lankafreshmart.lanka_fresh_mart.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceService {

    private final ExpenseRepository expenseRepository;
    private final OrderRepository orderRepository;

    public BigDecimal getTotalRevenue() {
        // Calculate sum of all confirmed/completed orders
        List<Order> orders = orderRepository.findAll();
        BigDecimal revenue = BigDecimal.ZERO;
        
        for (Order order : orders) {
            if (order.getStatus() == Order.Status.CONFIRMED) {
                revenue = revenue.add(order.getTotalAmount());
            }
        }
        return revenue;
    }

    public BigDecimal getTotalExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        BigDecimal total = BigDecimal.ZERO;
        
        for (Expense expense : expenses) {
            total = total.add(expense.getAmount());
        }
        return total;
    }

    public BigDecimal getNetProfit() {
        return getTotalRevenue().subtract(getTotalExpenses());
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    @Transactional
    public Expense addExpense(String description, BigDecimal amount) {
        Expense expense = new Expense();
        expense.setDescription(description);
        expense.setAmount(amount);
        return expenseRepository.save(expense);
    }

    @Transactional
    public Expense updateExpense(Long id, String description, BigDecimal amount) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        expense.setDescription(description);
        expense.setAmount(amount);
        return expenseRepository.save(expense);
    }

    @Transactional
    public void deleteExpense(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        expenseRepository.delete(expense);
    }
}
