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

        ArrayList<String> preparedNewDescriptions = prepareDescriptions(newTransactions);

        ArrayList<Transaction> existingTransactions = dbManager.readTransactions(null);
        ArrayList<String> preparedExistingDescriptions =
                prepareDescriptions(existingTransactions);

        NormalizedLevenshtein normalizedLevenshtein = new NormalizedLevenshtein();

        for (int i = 0; i < preparedNewDescriptions.size(); i++) {
            for (int j = 0; j < preparedExistingDescriptions.size(); j++) {

                // don't compare against uncategorized transactions
                if (existingTransactions.get(j).getCategoryId() != UNCATEGORIZED) {

                    // compare every new transaction to every existing transaction for similarity
                    Double similarity = normalizedLevenshtein.similarity(preparedNewDescriptions.get(i),
                            preparedExistingDescriptions.get(j));

                    // very similar
                    if (similarity > 0.4) {
                        newTransactions.get(i).setCategoryId(existingTransactions.get(j).getCategoryId());
                    }
                }
            }

            // if newTransaction still hasn't found a category, assign to uncategorized
            if (newTransactions.get(i).getCategoryId() == 0) {
                newTransactions.get(i).setCategoryId(UNCATEGORIZED);
            }

            dbManager.createTransaction(newTransactions.get(i));
        }
        return new ArrayList<>(dbManager.readTransactions(null).subList(0, newTransactions.size()));
    }

    /**
     * Prepares transactions for comparison by removing unnecassary characters from description.
     * Truncates all characters after '\', which is used primarily in 'BETAALAUTOMAAT' payments.
     * The part before '\' differs most, and is used for string comparison.
     *
     * @param transactions
     * @return
     */
    private static ArrayList<String> prepareDescriptions(ArrayList<Transaction> transactions) {
        ArrayList<String> preparedDescriptions = new ArrayList<>();
        for (Transaction transaction:transactions) {

            // format the description
            String newDescription = formatTransactionDescription(transaction.getDescription());

            // and add it to the list
            preparedDescriptions.add(newDescription);
        }

        return preparedDescriptions;
    }

    private static String formatTransactionDescription(String description) {
        String[] words = description.split(" ");
        ArrayList<String> newWords = new ArrayList<>();

        // if the last two words of the description are all uppercase, remove them from description
        for (int i = 0 ; i < words.length; i++) {

            // because this isn't a description: "AMSTERDAM NL"
            if (words.length < 3) {
                newWords.add(words[i]);
            }

            // but this is: "Something AMSTERDAM NL"
            else if (i < words.length - 2) {
                    newWords.add(words[i]);
                }

            // thus, if a descriptions contains at least 3 words, add only the words that aren't uppercase
            else if (!isUpperCase(words[i])) {
                    newWords.add(words[i]);
                }
        }

        // return the rebuilt description string for comparison
        return TextUtils.join(" ", newWords);
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
