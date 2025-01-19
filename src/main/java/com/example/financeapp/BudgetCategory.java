package com.example.financeapp;

import java.util.concurrent.atomic.AtomicLong;

public class BudgetCategory {
    private final AtomicLong income = new AtomicLong(0);
    private final AtomicLong expense = new AtomicLong(0);
    private final AtomicLong budget = new AtomicLong(0);
    private  AtomicLong leftBudget = new AtomicLong(0);

    public BudgetCategory() {
    }

    public void addBudget(double amount) {
        budget.addAndGet((long) amount);
    }

    public void addIncome(double amount) {
        income.addAndGet((long) amount);
    }

    public void addExpense(double amount) {
        expense.addAndGet((long) amount);
    }

    public long getIncome() {
        return income.get();
    }

    public long getExpense() {
        return expense.get();
    }

    public long getBudget() {
        return budget.get();
    }

    public long getLeftBudget() {
        return budget.get() - expense.get();
    }
}
