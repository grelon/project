package com.example.sander.bunqer.ModelClasses;
/*
 * Created by sander on 12-6-17.
 */

import android.content.Context;

import com.example.sander.bunqer.DB.DBManager;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable {
    /**
     * Defines category class
     */

    private int id;
    private int accountId;
    private String name;
    private ArrayList<Transaction> transactions;
    private int totalValue;

    // constructors
    public Category(int accountId, String name) {
        this.accountId = accountId;
        this.name = name;
    }

    public Category(int id, int accountId, String name) {
        this.id = id;
        this.accountId = accountId;
        this.name = name;
    }

    // setters & getters
    public int getTotalValue() {
        // if the transactions haven't been assigned to categories yet
        if (transactions == null) {
            updateTransactions();
        }

        totalValue = 0;
        for (Transaction transaction:getTransactions()) {
            totalValue += transaction.getAmount();
        }
        return totalValue;
    }

    private void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int account_id) {
        this.accountId = account_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // other
    private void updateTransactions() {
        // if transaction has not yet been initialized
        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        // iterate over every transaction and add them if they belong to this category
        transactions.clear();
        for (Transaction transaction:DBManager.getInstance().readTransactions(id)) {
            transactions.add(transaction);
        }
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", name='" + name + '\'' +
                '}';
    }
}
