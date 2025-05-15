package com.example.blushfinance.db;

public class Transaction {
    public final int id;
    public final String name;
    public final float amount;
    public final boolean isIncome;

    public Transaction(int id, String name, float amount, boolean isIncome) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.isIncome = isIncome;
    }
}
