package com.example.sander.bunqer.ModelClasses;
/*
 * Created by sander on 12-6-17.
 */

import android.util.Log;

import java.util.ArrayList;

public class Category {
    /**
     * Defines category class
     */

    private int id;
    private int account_id;
    private String name;
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private float totalValue = 0.00f;

    // constructors
    public Category(int account_id, String name) {
        this.account_id = account_id;
        this.name = name;
    }

    public Category(int id, int account_id, String name) {
        this.id = id;
        this.account_id = account_id;
        this.name = name;
    }

    // setters & getters
    public float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(float totalValue) {
        this.totalValue = totalValue;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
        for (Transaction transaction: transactions) {
            totalValue += transaction.getAmount();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // tostring
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", account_id=" + account_id +
                ", name='" + name + '\'' +
                '}';
    }
}
