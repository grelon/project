package com.example.sander.bunqer.Sorter;
/*
 * Created by sander on 12-6-17.
 *
 * Sorts transactions into categories.
 */


import android.content.Context;
import android.util.Log;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Account;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;
import com.example.sander.bunqer.R;

import java.util.ArrayList;

public class CategoryHelper {
    private static DBManager dbManager;

    public CategoryHelper(Context context) {
        this.dbManager = DBManager.getInstance(context);
    }

    public void categorize(ArrayList<Transaction> transactions) {
        for (Transaction transaction: transactions) {
            // check if we're dealing with a new account (i.e. account_id == 0)
            if(transaction.getAccount_id() == 0) {
                setupAccount(transaction);
            }

            // otherwise categorize in uncategorized
            transaction.setCategory_id(1);
            dbManager.createTransaction(transaction);
        }
    }

    private void setupAccount(Transaction transaction) {
        Account newAccount = new Account(transaction.getAccount(), "test");
        dbManager.createAccount(newAccount);
        Account latestAccount = dbManager.readAccounts().get(dbManager.readAccounts().size());
        setupDefaultCategories(latestAccount);
    }

    private void setupDefaultCategories(Account account) {
        Category uncategorized = new Category(account.getId(), "Uncategorized");
        dbManager.createCategory(uncategorized);
    }
}
