package com.example.sander.bunqer.Helpers;
/*
 * Created by sander on 12-6-17.
 *
 * Sorts transactions into categories and actually writes them to the database for the first time.
 */


import android.content.Context;
import android.util.Log;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class CategoryHelper {
    private static DBManager dbManager;
    private static CategoryHelper catHelper;

    private CategoryHelper(Context context) {
        dbManager = DBManager.getInstance(context);
    }

    static synchronized CategoryHelper getInstance(Context context) {
        Log.d("log", "ch.getInstance");
        if (catHelper == null) {
            Log.d("log", "new instance");
            catHelper = new CategoryHelper(context);
        }
        Log.d("log", "old instance");
        return catHelper;
    }

    ArrayList<Transaction> categorize(ArrayList<Transaction> transactions) {
        Log.d("log", "start categorize()");
        ArrayList<Category> categories = dbManager.readCategories();
        for (Transaction transaction: transactions) {
            // TODO: 13-6-17 write initial categorization algorithm
            // to income uncategorized
            if (transaction.getAmount() >= 0) {
                transaction.setCategory_id(categories.get(0).getId());
                transaction.setCategory(categories.get(0).getName());
            }
            // to expenses uncategorized
            else {
                transaction.setCategory_id(categories.get(1).getId());
                transaction.setCategory(categories.get(1).getName());
            }
            dbManager.createTransaction(transaction);
        }
        return dbManager.readTransactions();
    }

}
