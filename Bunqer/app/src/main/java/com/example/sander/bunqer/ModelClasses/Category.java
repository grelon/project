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
    private int parentId;
    private String name;
    private ArrayList<Category> subcategories = new ArrayList<>();
    private ArrayList<Transaction> transactions;
    private int totalValue;

    // constructors
    public Category() {
    }

    public Category(int accountId, int parentId, String name) {
        this.accountId = accountId;
        this.parentId = parentId;
        this.name = name;
    }

    // constructor used by database
    public Category(int id, int parentId, int accountId, String name) {
        this.id = id;
        this.accountId = accountId;
        this.parentId = parentId;
        this.name = name;
    }

    // setters & getters
    public ArrayList<Category> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<Category> subcategories) {
        this.subcategories = subcategories;
    }

    public void addSubcategory(Category subCategory) {
        this.subcategories.add(subCategory);
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public int getTotalValue() {
        // initialize total value of category at 0 eurocents
        totalValue = 0;

        // recursively get total value of subcategories
        if (subcategories.size() > 0) {
            for (Category category:subcategories) {
                totalValue += category.getTotalValue();
            }
        }

        // if the transactions haven't been assigned to categories yet
        if (transactions.isEmpty()) {
            updateTransactions();
        }

        // add all transactions of category to total value
        for (Transaction transaction:getTransactions()) {
            totalValue += transaction.getAmount();
        }
        return totalValue;
    }

    private void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public ArrayList<Transaction> getTransactions() {
        updateTransactions();
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
    public void updateTransactions() {
        // if transactions have not yet been initialized
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
