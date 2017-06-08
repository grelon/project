package com.example.sander.bunqer;
/*
 * Created by sander on 8-6-17.
 */

public class Transaction {
    private String counterparty;
    private String amount;
    private String date;

    public Transaction() {
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
