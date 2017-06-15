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
        if (catHelper == null) {
            catHelper = new CategoryHelper(context);
        }
        return catHelper;
    }

    ArrayList<Transaction> categorize(ArrayList<Transaction> transactions) {
        Log.d("log", "start categorize()");
        ArrayList<Category> categories = dbManager.readCategories();
        for (Transaction transaction: transactions) {
            // TODO: 13-6-17 write initial categorization algorithm
            // to income
            if (transaction.getDescription().contains("Welcome to bunq")) {
                transaction.setCategory_id(categories.get(1).getId());
                transaction.setCategory(categories.get(1).getName());
            }

            // to household
            else if (transaction.getDescription().contains("IKEA") ||
                    transaction.getDescription().contains("PRAXIS") ||
                    transaction.getDescription().contains("Albert Heijn") ||
                    transaction.getDescription().contains("Lidl") ||
                    transaction.getDescription().contains("AH")) {
                transaction.setCategory_id(categories.get(2).getId());
                transaction.setCategory(categories.get(2).getName());
            }

            // to sports
            else if (transaction.getDescription().contains("karate")) {
                transaction.setCategory_id(categories.get(3).getId());
                transaction.setCategory(categories.get(3).getName());
            }

            // to food and drinks
            else if (transaction.getDescription().contains("cafe") ||
                    transaction.getDescription().contains("restaurant") ||
                    transaction.getDescription().contains("UvA") ||
                    transaction.getDescription().contains("Cafe") ||
                    transaction.getDescription().contains("Restaurant")) {
                transaction.setCategory_id(categories.get(4).getId());
                transaction.setCategory(categories.get(4).getName());
            }

            // to uncategorized
            else {
                transaction.setCategory_id(categories.get(0).getId());
                transaction.setCategory(categories.get(0).getName());
            }
            dbManager.createTransaction(transaction);
        }
        return dbManager.readTransactions();
    }

}
