package com.example.sander.bunqer.ModelClasses;
/*
 * Created by sander on 8-6-17.
 */

public class Transaction {
    private int id;
    private String date;
    private float amount;
    private int account_id;
    private String account;
    private String category;
    private int category_id;
    private String counterparty_name;
    private String counterparty_account;
    private String description;

    // constructor for CSV import


    public Transaction() {
    }

    // constructor for DB
    public Transaction(int id, String date, float amount, int account_id, String account,
                       String category, int category_id, String counterparty_name,
                       String counterparty_account, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.account_id = account_id;
        this.account = account;
        this.category = category;
        this.category_id = category_id;
        this.counterparty_name = counterparty_name;
        this.counterparty_account = counterparty_account;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getCounterparty_name() {
        return counterparty_name;
    }

    public void setCounterparty_name(String counterparty_name) {
        this.counterparty_name = counterparty_name;
    }

    public String getCounterparty_account() {
        return counterparty_account;
    }

    public void setCounterparty_account(String counterparty_account) {
        this.counterparty_account = counterparty_account;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                ", account_id=" + account_id +
                ", account='" + account + '\'' +
                ", category='" + category + '\'' +
                ", category_id=" + category_id +
                ", counterparty_name='" + counterparty_name + '\'' +
                ", counterparty_account='" + counterparty_account + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
