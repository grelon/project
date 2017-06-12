package com.example.sander.bunqer.ModelClasses;
/*
 * Created by sander on 12-6-17.
 */

public class Account {
    /**
     * Defines Account class
     */

    private int id;
    private String number;
    private String name;

    // constructors
    public Account(int id, String account_number, String name) {
        this.id = id;
        this.number = account_number;
        this.name = name;
    }

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
