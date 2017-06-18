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

import info.debatty.java.stringsimilarity.Jaccard;

public class CategoryHelper {
    private static DBManager dbManager;
    private static CategoryHelper catHelper;

    private CategoryHelper(Context context) {
        dbManager = DBManager.getInstance();
    }

    static synchronized CategoryHelper getInstance(Context context) {
        if (catHelper == null) {
            catHelper = new CategoryHelper(context);
        }
        return catHelper;
    }

    ArrayList<Transaction> categorize(ArrayList<Transaction> transactions) {
        Log.d("log", "start categorize()");

        ArrayList<Category> categories = dbManager.readCategories(null);
        Jaccard jaccardIndex = new Jaccard();
        Log.d("log", "jaccard ah VS ah: " + jaccardIndex.similarity(
                "ALBERT HEIJN 1090 \\AMSTERDAM \\ BETAALAUTOMAAT 06-06-17 21:15 PASNR.102 CONTACTLOOS",
                "ALBERT HEIJN 2490 \\BREDA \\ BETAALAUTOMAAT 14-11-16 12:25 PASNR.103 CONTACTLOOS"));
        Log.d("log", "jaccard hio asdf: " + jaccardIndex.similarity("hio", "asdf"));

        for (Transaction transaction: transactions) {
            // TODO: 13-6-17 write initial categorization algorithm
            // to income
            if (transaction.getDescription().contains("Welcome to bunq")) {
                transaction.setCategoryId(categories.get(1).getId());
            }

            // to household
            else if (transaction.getDescription().contains("IKEA") ||
                    transaction.getDescription().contains("PRAXIS") ||
                    transaction.getDescription().contains("Albert Heijn") ||
                    transaction.getDescription().contains("Lidl") ||
                    transaction.getDescription().contains("AH")) {
                transaction.setCategoryId(categories.get(2).getId());
            }

            // to sports
            else if (transaction.getDescription().contains("karate")) {
                transaction.setCategoryId(categories.get(3).getId());
            }

            // to food and drinks
            else if (transaction.getDescription().contains("cafe") ||
                    transaction.getDescription().contains("restaurant") ||
                    transaction.getDescription().contains("UvA") ||
                    transaction.getDescription().contains("Cafe") ||
                    transaction.getDescription().contains("Restaurant")) {
                transaction.setCategoryId(categories.get(4).getId());
            }

            // to uncategorized
            else {
                transaction.setCategoryId(categories.get(0).getId());
            }
            dbManager.createTransaction(transaction);
        }
        return dbManager.readTransactions(null);
    }

}
