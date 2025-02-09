package com.example.task.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.example.task.model.Expense;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double budget; // Buxheti për këtë kategori
    private Double currentExpenses = 0.0; // Shpenzimet aktuale për këtë kategori

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @JsonIgnore
    private List<Expense> expenses = new ArrayList<>();

    // Getters dhe Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public Double getCurrentExpenses() {
        return currentExpenses;
    }

    public void setCurrentExpenses(Double currentExpenses) {
        this.currentExpenses = currentExpenses;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
    public void addExpense(Expense expense) {
        expenses.add(expense);
        expense.setCategory(this); // Lidh shpenzimin me kategorinë
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
        expense.setCategory(null);
    }

}
