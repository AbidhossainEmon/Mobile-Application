package com.example.emon.tourmate.Other_class;

public class Expense {
    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    private String description,id;
    int amount;

    public Expense() {
    }

    public Expense(String description, String id, int amount) {
        this.description = description;
        this.id = id;
        this.amount = amount;
    }
}
