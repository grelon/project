package com.example.sander.bunqer.ModelClasses;
/*
 * Created by sander on 8-6-17.
 */

import com.example.sander.bunqer.DB.DBManager;

import java.io.Serializable;

public class Transaction implements Serializable {
    private int id;
    private String date;
    private int amount;
    private int accountId;
    private String account;
    private String category;
    private int categoryId = 0;
    private String counterpartyName;
    private String counterpartyAccount;
    private String description;

    // constructor for CSV import
    public Transaction() {
    }

    // constructor for DB
    public Transaction(int id, String date, int amount, int accountId, String account,
                       int categoryId, String counterpartyName,
                       String counterpartyAccount, String description) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.accountId = accountId;
        this.account = account;
        this.categoryId = categoryId;
        this.counterpartyName = counterpartyName;
        this.counterpartyAccount = counterpartyAccount;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Category name is assigned when first called upon only, to prevent an infinite loop when
     * creating categories from database.
     *
     * @return
     */
    public String getCategory() {
        if (category == null) {
            category = DBManager.getInstance().readCategories(categoryId).get(0).getName();
        }
        return category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public String getCounterpartyAccount() {
        return counterpartyAccount;
    }

    public void setCounterpartyAccount(String counterpartyAccount) {
        this.counterpartyAccount = counterpartyAccount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = Integer.parseInt((amount.replace(".","").replace(",","")));
    }

    public void setAmount(int amount) {
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
                ", accountId=" + accountId +
                ", account='" + account + '\'' +
                ", category='" + category + '\'' +
                ", categoryId=" + categoryId +
                ", single_transaction_counterpartyName='" + counterpartyName + '\'' +
                ", single_transaction_counterpartyAccount='" + counterpartyAccount + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getFormattedAmount() {
        String formattedAmount = String.valueOf(amount);
        // TODO: 20-6-17 Format amount with a StringBuilder or something like that.
        return formattedAmount;
    }
}
