package com.example.task.model;

import jakarta.persistence.*;

@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double totalBudget; // Buxheti total
    private Double currentExpenses = 0.0; // Shpenzimet aktuale (fillon me 0)

    // Konstruktori bosh
    public Budget() {}

    // Getters dhe Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(Double totalBudget) {
        this.totalBudget = totalBudget;
    }

    public Double getCurrentExpenses() {
        return currentExpenses;
    }

    public void setCurrentExpenses(Double currentExpenses) {
        this.currentExpenses = currentExpenses;
    }
}

