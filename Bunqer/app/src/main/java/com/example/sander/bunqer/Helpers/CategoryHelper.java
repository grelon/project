package com.example.sander.bunqer.Helpers;
/*
 * Created by sander on 12-6-17.
 *
 * Sorts transactions into categories and actually writes them to the database for the first time.
 */


import android.text.TextUtils;
import android.util.Log;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Account;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;
import java.util.Arrays;

import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

public class CategoryHelper {
    public static final int UNCATEGORIZED = 1;
    public static final int INCOME = 2;
    public static final int EXPENSES = 3;
    public static final int GIFT = 4;
    public static final int HOUSEHOLD = 5;
    public static final int ROOT = -1;

    private static DBManager dbManager;

    private CategoryHelper(){}

    static ArrayList<Transaction> categorize(ArrayList<Transaction> newTransactions) {
        Log.d("log", "start categorize()");

        // make sure there is a DBManager instance
        if (dbManager == null) {
            dbManager = DBManager.getInstance();
        }

        ArrayList<Transaction> preparedNewTransactions = prepareTransactions(newTransactions);
        Log.d("log", "new transactions: " + preparedNewTransactions.toString());

        ArrayList<Transaction> preparedExistingTransactions =
                prepareTransactions(dbManager.readTransactions(null));
        Log.d("log", "existing transactions: " + preparedExistingTransactions.toString());

        NormalizedLevenshtein normalizedLevenshtein = new NormalizedLevenshtein();

        int i = 0;
        for (Transaction newTransaction: preparedNewTransactions) {
            for (Transaction existingTransaction:preparedExistingTransactions) {

                // don't compare against uncategorized transactions
                if (existingTransaction.getCategoryId() != UNCATEGORIZED) {

                    // compare every new transaction to every existing transaction for similarity
                    Double similarity = normalizedLevenshtein.similarity(newTransaction.getDescription(),
                            existingTransaction.getDescription());

                    // very similar
                    if (similarity > 0.4) {
                        newTransactions.get(i).setCategoryId(existingTransaction.getCategoryId());
                    }
                }
            }

            // if newTransaction still hasn't found a category, assign to uncategorized
            if (newTransactions.get(i).getCategoryId() == 0) {
                newTransactions.get(i).setCategoryId(UNCATEGORIZED);
            }

            dbManager.createTransaction(newTransactions.get(i));
            i++;
        }
        return dbManager.readTransactions(null);
    }

    /**
     * Prepares transactions for comparison by removing unnecassary characters from description.
     * Truncates all characters after '\', which is used primarily in 'BETAALAUTOMAAT' payments.
     * The part before '\' differs most, and is used for string comparison.
     *
     * @param transactions
     * @return
     */
    private static ArrayList<Transaction> prepareTransactions(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> preparedTransactions = Arrays.copyOf(transactions);

        for (Transaction transaction:preparedTransactions) {
            // remove words with all caps
            String newDescription = formatTransactionDescription(transaction.getDescription());

            // update description a working format for categorize
            transaction.setDescription(newDescription);
        }

        return preparedTransactions;
    }

    private static String formatTransactionDescription(String description) {
        String[] words = description.split(" ");
        ArrayList<String> newWords = new ArrayList<>();

        // if the last three words of the description are all uppercase, remove them from description
        for (int i = 0 ; i < words.length; i++) {

            // because this isn't a description: "AMSTERDAM NL"
            if (words.length < 3) {
                newWords.add(words[i]);
            }

            // but this is: "Something AMSTERDAM NL"
            else if (i < words.length - 2) {
                    newWords.add(words[i]);
                }
                else if (!isUpperCase(words[i])) {
                        newWords.add(words[i]);
                    }

//            if (i < words.length - 2 || !isUpperCase(words[i])) {
//                newWords.add(words[i]);
//                }
        }

        String newDescription = TextUtils.join(" ", newWords);

        Log.d("log", "newDescription: " + newDescription);
        return newDescription;
    }

    public static void setupDefaultCategories(Account newAccount) {
        // make sure there is a DBManager instance
        if (dbManager == null) {
            dbManager = DBManager.getInstance();
        }

        // list of root category names
        ArrayList<String> defaultRootNames = new ArrayList<>();
        defaultRootNames.add("Uncategorized");
        defaultRootNames.add("Income");
        defaultRootNames.add("Expenses");

        // create root categories
        for (String name:defaultRootNames) {
            dbManager.createCategory(new Category(newAccount.getId(), ROOT, name));
        }

        // list of default subcategory names
        ArrayList<Category> defaultCategories = new ArrayList<>();
        defaultCategories.add(new Category(newAccount.getId(), INCOME, "Salary"));
        defaultCategories.add(new Category(newAccount.getId(), INCOME, "Scholarship"));
        defaultCategories.add(new Category(newAccount.getId(), INCOME, "Benefits"));
        defaultCategories.add(new Category(newAccount.getId(), INCOME, "Other income"));

        defaultCategories.add(new Category(newAccount.getId(), EXPENSES, "Household"));
        defaultCategories.add(new Category(newAccount.getId(), EXPENSES, "Housing costs"));
        defaultCategories.add(new Category(newAccount.getId(), EXPENSES, "Subscriptions"));
        defaultCategories.add(new Category(newAccount.getId(), EXPENSES, "Transport"));
        defaultCategories.add(new Category(newAccount.getId(), EXPENSES, "Food and drinks"));
        defaultCategories.add(new Category(newAccount.getId(), EXPENSES, "Other expenses"));

        // create default subcategories
        for (Category category: defaultCategories) {
            dbManager.createCategory(category);
        }
    }

    /**
     * Checks if a string is all uppercase. Stole it from: https://stackoverflow.com/a/677592
     */
    public static boolean isUpperCase(String s)
    {
        for (int i=0; i<s.length(); i++)
        {
            if (!Character.isUpperCase(s.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }
}
