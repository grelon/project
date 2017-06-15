package com.example.sander.bunqer.ModelClasses;
/*
 * Created by sander on 12-6-17.
 */

import android.content.Context;

import com.example.sander.bunqer.DB.DBManager;

import java.util.ArrayList;

public class Category {
    /**
     * Defines category class
     */

    private int id;
    private int accountId;
    private String name;
    private ArrayList<Transaction> transactions = new ArrayList<>();
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

    public void updateTransactions(Context context) {
        // iterate over every transaction and add them if they belong to this category
        transactions.clear();
        for (Transaction transaction:DBManager.getInstance(context).readTransactions()) {
            if (transaction.getCategoryId() == this.getId()) {
                transactions.add(transaction);
            }
        }
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

    // tostring
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", name='" + name + '\'' +
                '}';
    }
}
