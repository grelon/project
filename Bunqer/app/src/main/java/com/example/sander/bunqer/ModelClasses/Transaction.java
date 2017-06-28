package com.example.sander.bunqer.ModelClasses;
/*
 * Created by sander on 8-6-17.
 */

import com.example.sander.bunqer.DB.DBManager;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Defines the Transaction class and a few related methods.
 */

public class Transaction implements Serializable, Cloneable{
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

    // constructor
    public Transaction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets category name. This is assigned when first called upon only, to prevent an infinite loop when
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

    public void setCategory(String category) {
        this.category = category;
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
                ", counterpartyName='" + counterpartyName + '\'' +
                ", counterpartyAccount='" + counterpartyAccount + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    /**
     * Returns amount formatted as an amount in euros.
     * @return
     */
    public String getFormattedAmount() {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return format.format(amount/100);
    }

    /**
     * Returns false if a transaction already exists in the DB.
     * @return
     */
    public boolean isNotDuplicate() {
        ArrayList<Transaction> transactions = DBManager.getInstance().readTransactions(null);

        // check this transaction against all other transactions
        if (transactions.size() > 0) {
            for (Transaction transaction : transactions) {
                if (transaction.getDate().equals(this.date) &&
                        transaction.getAmount() == this.amount &&
                        transaction.getDescription().equals(this.description) &&
                        transaction.getCounterpartyAccount().equals(this.counterpartyAccount)) {

                    // transaction is a duplicate
                    return false;
                }
            }
        }

        // transaction is not a duplicate
        return true;
    }
}
