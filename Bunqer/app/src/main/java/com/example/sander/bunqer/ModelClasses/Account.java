package com.example.sander.bunqer.ModelClasses;
/*
 * Created by sander on 12-6-17.
 */

/**
 * Defines Account class. Accounts are primarily set up for future versions that support multiple
 * accounts.
 */
public class Account {

    private int id;
    private String number;
    private String name;

    // constructor
    public Account() {
    }

    // constructor for setting up new accounts
    public Account(String account_number, String name) {
        this.number = account_number;
        this.name = name;
    }

    // getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // toString
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
