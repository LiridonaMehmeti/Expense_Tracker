package com.example.task.repository;

import com.example.task.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	
    List<Expense> findByCategory_Name(String categoryName);

    List<Expense> findByDateBetween(LocalDate fromDate, LocalDate toDate);
    
    @Query("SELECT e FROM Expense e WHERE e.amount = (SELECT MAX(e2.amount) FROM Expense e2)")
    Expense findMostExpensiveExpense();

    
    
}