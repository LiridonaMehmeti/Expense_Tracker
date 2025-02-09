package com.example.task.service;

import com.example.task.controller.ExpenseController;


import com.example.task.model.Category;
import com.example.task.model.Expense;
import com.example.task.model.Budget;
import com.example.task.repository.CategoryRepository;
import com.example.task.repository.ExpenseRepository;
import com.example.task.repository.BudgetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

import org.springframework.format.annotation.DateTimeFormat; // Për parametrat e datës

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	
	private static final Logger log = LoggerFactory.getLogger(ExpenseController.class);

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository; // Injeksioni i CategoryRepository
    private final BudgetRepository budgetRepository;
    
    @Value("${app.total-budget}")
    private double totalBudget;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, CategoryRepository categoryRepository,BudgetRepository budgetRepository ) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.budgetRepository = budgetRepository;
    }

     /*@Override
    public Expense createExpense(Expense expense) {
        // Kontrollo nëse kategoria ekziston
        Category category = categoryRepository.findById(expense.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found!"));

        // Kontrollo buxhetin e kategorisë
        if (category.getCurrentExpenses() + expense.getAmount() > category.getBudget()) {
            throw new RuntimeException("Category budget exceeded!");
        }

        //Përditëso shpenzimet aktuale të kategorisë
        category.setCurrentExpenses(category.getCurrentExpenses() + expense.getAmount());
        categoryRepository.save(category);
        
        if (expense.getCategory() == null || expense.getCategory().getId() == null) {
            throw new RuntimeException("Category must be provided!");
        }


        // Ruaj shpenzimin
        return expenseRepository.save(expense);
    }*/
   /* @Override
    public Expense createExpense(Expense expense) {
        // Kontrollo nëse kategoria është e vlefshme
        if (expense.getCategory() == null || expense.getCategory().getId() == null) {
            throw new RuntimeException("Category must be provided and have a valid ID!");
        }

        // Kontrollo nëse kategoria ekziston në databazë
        Category category = categoryRepository.findById(expense.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found!"));

        // Kontrollo buxhetin e kategorisë
        if (category.getCurrentExpenses() + expense.getAmount() > category.getBudget()) {
            throw new RuntimeException("Category budget exceeded!");
        }

        // Përditëso shpenzimet aktuale të kategorisë
        category.setCurrentExpenses(category.getCurrentExpenses() + expense.getAmount());
        categoryRepository.save(category);

        // Ruaj shpenzimin në databazë
        return expenseRepository.save(expense);
    }*/
    @Override
    public Expense createExpense(Expense expense) {
        double totalExpenses = expenseRepository.findAll()
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        if (totalExpenses + expense.getAmount() > totalBudget) {
            throw new RuntimeException("Total budget exceeded!");
        }

        // Kontrolli për kategorinë
        if (expense.getCategory() == null || expense.getCategory().getId() == null) {
            throw new RuntimeException("Category must be provided and have a valid ID!");
        }

        Category category = categoryRepository.findById(expense.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found!"));

        if (category.getCurrentExpenses() + expense.getAmount() > category.getBudget()) {
            throw new RuntimeException("Category budget exceeded!");
        }

        category.setCurrentExpenses(category.getCurrentExpenses() + expense.getAmount());
        categoryRepository.save(category);

        return expenseRepository.save(expense);
    }


    /*@Override
    public Expense createExpense(Expense expense) {
        // Kontrollo nëse kategoria ekziston
        Category category = categoryRepository.findById(expense.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found!"));

        // Kontrollo buxhetin e kategorisë
       if (category.getCurrentExpenses() + expense.getAmount() > category.getBudget()) {
            log.error("Category budget exceeded for category: {}, current expenses: {}, amount: {}",
                      category.getName(), category.getCurrentExpenses(), expense.getAmount());
            throw new RuntimeException("Category budget exceeded!");
        }

        // Kontrollo buxhetin e përgjithshëm
       double totalExpenses = expenseRepository.findAll()
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();

        if (totalExpenses + expense.getAmount() > totalBudget) {
            throw new RuntimeException("Total budget exceeded!");
        }
        

        // Përditëso shpenzimet e kategorisë
        category.setCurrentExpenses(category.getCurrentExpenses() + expense.getAmount());
        categoryRepository.save(category);

        // Ruaj shpenzimin në databazë
        return expenseRepository.save(expense);
    }*/


    @Override
    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    @Override
    public List<Expense> getAllExpenses() {
        log.info("Retrieving all expenses from database...");
        return expenseRepository.findAll();
    }

    @Override
    public Expense updateExpense(Long id, Expense expense) {
        if (expenseRepository.existsById(id)) {
            expense.setId(id);
            return expenseRepository.save(expense);
        }
        throw new RuntimeException("Expense not found!");
    }

    @Override
    public void deleteExpense(Long id) {
        if (expenseRepository.existsById(id)) {
            expenseRepository.deleteById(id);
        } else {
            throw new RuntimeException("Expense not found!");
        }
    }

    public double getTotalExpenses() {
        return expenseRepository.findAll()
                .stream()
                .mapToDouble(Expense::getAmount)
                .sum();
    }
    @Override
    public Expense getMostExpensiveExpense() {
        return expenseRepository.findMostExpensiveExpense();
    }
    @Override
    public Optional<Expense> getLeastExpensiveExpense(Long categoryId, LocalDate fromDate, LocalDate toDate) {
        List<Expense> expenses = expenseRepository.findAll();

        // Apply filters based on optional parameters
        if (categoryId != null) {
            expenses = expenses.stream()
                    .filter(expense -> expense.getCategory().getId().equals(categoryId))
                    .toList();
        }

        if (fromDate != null) {
            expenses = expenses.stream()
                    .filter(expense -> !expense.getDate().isBefore(fromDate))
                    .toList();
        }

        if (toDate != null) {
            expenses = expenses.stream()
                    .filter(expense -> !expense.getDate().isAfter(toDate))
                    .toList();
        }

        // Find the least expensive expense
        return expenses.stream().min(Comparator.comparingDouble(Expense::getAmount));
    }
    @Override
    public double calculateAverageDailyExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        if (expenses.isEmpty()) {
            return 0.0;
        }

        LocalDate earliestDate = expenses.stream()
                .min(Comparator.comparing(Expense::getDate))
                .get()
                .getDate();

        LocalDate latestDate = expenses.stream()
                .max(Comparator.comparing(Expense::getDate))
                .get()
                .getDate();

        long daysBetween = ChronoUnit.DAYS.between(earliestDate, latestDate) + 1; // +1 për të përfshirë të dyja datat
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();

        return totalExpenses / daysBetween;
    }
    @Override
    public double calculateAverageMonthlyExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        if (expenses.isEmpty()) {
            return 0.0;
        }

        LocalDate earliestDate = expenses.stream()
                .min(Comparator.comparing(Expense::getDate))
                .get()
                .getDate();

        LocalDate latestDate = expenses.stream()
                .max(Comparator.comparing(Expense::getDate))
                .get()
                .getDate();

        long monthsBetween = ChronoUnit.MONTHS.between(
                YearMonth.from(earliestDate),
                YearMonth.from(latestDate)
        ) + 1; // +1 për të përfshirë të dyja muajt

        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();

        return totalExpenses / monthsBetween;
    }
    @Override
    public double calculateAverageYearlyExpenses() {
        List<Expense> expenses = expenseRepository.findAll();
        if (expenses.isEmpty()) {
            return 0.0;
        }

        LocalDate earliestDate = expenses.stream()
                .min(Comparator.comparing(Expense::getDate))
                .get()
                .getDate();

        LocalDate latestDate = expenses.stream()
                .max(Comparator.comparing(Expense::getDate))
                .get()
                .getDate();

        long yearsBetween = ChronoUnit.YEARS.between(
                Year.from(earliestDate),
                Year.from(latestDate)
        ) + 1; // +1 për të përfshirë të dyja vitet

        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();

        return totalExpenses / yearsBetween;
    }


}
