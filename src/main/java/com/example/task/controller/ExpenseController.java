package com.example.task.controller;

import com.example.task.model.Expense;
import com.example.task.service.ExpenseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

	private static final Logger log = LoggerFactory.getLogger(ExpenseController.class);
    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    // Endpoint për krijimin e një Expense
    /*@PostMapping
    public ResponseEntity<?> createExpense(@Valid @RequestBody Expense expense) {
        return new ResponseEntity<>(expense, HttpStatus.CREATED);
    }*/
    @PostMapping
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense) {
        log.info("Received expense to create: {}", expense); // Kontrollo të dhënat në hyrje
        Expense createdExpense = expenseService.createExpense(expense); // Thirr metoda e shërbimit
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }


    // Endpoint për marrjen e një Expense nga ID
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        Optional<Expense> expense = expenseService.getExpenseById(id);
        return expense.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint për marrjen e të gjitha Expenses
    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        log.info("Fetching all expenses...");
        List<Expense> expenses = expenseService.getAllExpenses();
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }


    // Endpoint për përditësimin e një Expense
    @PutMapping("/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable Long id, @Valid @RequestBody Expense expense) {
        Expense updatedExpense = expenseService.updateExpense(id, expense); // Përditëso Expense
        return ResponseEntity.ok(updatedExpense); // Kthe Expense e përditësuar
    }

    // Endpoint për fshirjen e një Expense
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id); // Fshij Expense sipas ID
        return ResponseEntity.noContent().build(); // Kthe statusin 204
    }

    @GetMapping("/total-expenses")
    public ResponseEntity<Double> getTotalExpenses() {
        double totalExpenses = expenseService.getTotalExpenses();
        return ResponseEntity.ok(totalExpenses);
    }
    @GetMapping("/most-expensive")
    public ResponseEntity<Expense> getMostExpensiveExpense() {
        Expense mostExpensiveExpense = expenseService.getMostExpensiveExpense();
        return new ResponseEntity<>(mostExpensiveExpense, HttpStatus.OK);
    }

    @GetMapping("/least-expensive")
    public ResponseEntity<Expense> getLeastExpensiveExpense(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
        Optional<Expense> expense = expenseService.getLeastExpensiveExpense(categoryId, fromDate, toDate);
        if (expense.isPresent()) {
            return ResponseEntity.ok(expense.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @GetMapping("/average/daily")
    public ResponseEntity<Double> getAverageDailyExpenses() {
        double averageDailyExpenses = expenseService.calculateAverageDailyExpenses();
        return ResponseEntity.ok(averageDailyExpenses);
    }
    @GetMapping("/average/monthly")
    public ResponseEntity<Double> getAverageMonthlyExpenses() {
        double averageMonthlyExpenses = expenseService.calculateAverageMonthlyExpenses();
        return ResponseEntity.ok(averageMonthlyExpenses);
    }
    @GetMapping("/average/yearly")
    public ResponseEntity<Double> getAverageYearlyExpenses() {
        double averageYearlyExpenses = expenseService.calculateAverageYearlyExpenses();
        return ResponseEntity.ok(averageYearlyExpenses);
    }
    
}
