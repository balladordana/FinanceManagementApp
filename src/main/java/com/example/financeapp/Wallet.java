package com.example.financeapp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class Wallet {
    private String user;
    private final AtomicLong totalIncome = new AtomicLong(0);
    private final AtomicLong totalExpense = new AtomicLong(0);
    private  Map<String, BudgetCategory> categories = new ConcurrentHashMap<>();

    public Wallet(String user) {
        this.user = user;
    }

    public String getUser(){
        return user;
    }

    public void addIncome(String category, double amount) {
        categories.putIfAbsent(category, new BudgetCategory());
        categories.get(category).addIncome(amount);
        totalIncome.addAndGet((long) amount);
    }

    public void addExpense(String category, double amount) {
        categories.putIfAbsent(category, new BudgetCategory());
        categories.get(category).addExpense(amount);
        totalExpense.addAndGet((long) amount);
    }

    public void addBudget(String category, double amount) {
        categories.putIfAbsent(category, new BudgetCategory());
        categories.get(category).addBudget(amount);
    }

    public Map<String, BudgetCategory> getCategories() {
        return categories;
    }

    public long getTotalIncome() {
        return totalIncome.get();
    }

    public long getTotalExpense() {
        return totalExpense.get();
    }
}
