package com.example.task.service;

import com.example.task.model.Expense;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat; // Për parametrat e datës

import java.util.List;
import java.util.Optional;

public interface ExpenseService {

    Expense createExpense(Expense expense);
    
    double getTotalExpenses();

    Expense getMostExpensiveExpense();
    
    Optional<Expense> getLeastExpensiveExpense(Long categoryId, LocalDate fromDate, LocalDate toDate);
    
    double calculateAverageDailyExpenses();
    
    double calculateAverageMonthlyExpenses();
    
    double calculateAverageYearlyExpenses();

    Optional<Expense> getExpenseById(Long id);

    List<Expense> getAllExpenses();

    Expense updateExpense(Long id, Expense expense);

    void deleteExpense(Long id);
}
