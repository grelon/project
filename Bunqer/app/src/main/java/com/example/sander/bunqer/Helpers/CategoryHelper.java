package com.example.sander.bunqer.Helpers;
/*
 * Created by sander on 12-6-17.
 *
 * Sorts transactions into categories and actually writes them to the database for the first time.
 */


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
    public static final int HOUSEHOLD = 4;

    private static DBManager dbManager;
    private static CategoryHelper catHelper;

    private CategoryHelper() {
        dbManager = DBManager.getInstance();
    }

    public static synchronized CategoryHelper getInstance() {
        if (catHelper == null) {
            catHelper = new CategoryHelper();
        }
        return catHelper;
    }

    ArrayList<Transaction> categorize(ArrayList<Transaction> newTransactions) {
        Log.d("log", "start categorize()");

        // put test transaction in DB
        Transaction testTransaction = new Transaction();
        testTransaction.setDate("2017-06-01");
        testTransaction.setAmount("-13,12");
        testTransaction.setAccount("NL96BUNQ9900021843");
        testTransaction.setCounterpartyAccount("");
        testTransaction.setCounterpartyName("");
        testTransaction.setCounterpartyName("");
        testTransaction.setDescription("ALBERT HEIJN 1090 \\AMSTERDAM \\ BETAALAUTOMAAT 06-06-17 21:15 PASNR.102 CONTACTLOOS");
        testTransaction.setAccountId(CsvImportHelper.getAccountId(testTransaction));
        // to household
        testTransaction.setCategoryId(4);
        dbManager.createTransaction(testTransaction);
        Log.d("log", "testTransaction: " + testTransaction.toString());

        ArrayList<Transaction> preparedNewTransactions = prepareTransactions(newTransactions);
        Log.d("log", "new transactions: " + newTransactions.toString());

        ArrayList<Transaction> preparedExistingTransactions =
                prepareTransactions(dbManager.readTransactions(null));
        Log.d("log", "existing transactions: " + preparedExistingTransactions.toString());


        ArrayList<Category> categories = dbManager.readCategories(null);
        NormalizedLevenshtein normalizedLevenshtein = new NormalizedLevenshtein();
//        Log.d("log", "jaccard zelfde categorie: " + normalizedLevenshtein.similarity(
//                "ALBERT HEIJN 1090 \\",
//                "AH Station Amstel \\"));
//        Log.d("log", "jaccard andere categorie: " + normalizedLevenshtein.similarity(
//                "ALBERT HEIJN 1090 \\",
//                "8010-239-UvAScience \\"));


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
    private ArrayList<Transaction> prepareTransactions(ArrayList<Transaction> transactions) {
        ArrayList<Transaction> preparedTransactions = transactions;

        for (Transaction transaction:preparedTransactions) {
            String description = transaction.getDescription();
            if (description.contains("\\")) {
                transaction.setDescription(description.substring(0, description.indexOf("\\")));
            }
        }

        return preparedTransactions;
    }

    public static void setupDefaultCategories(Account newAccount) {
        // list of root category names
        ArrayList<String> defaultRootNames = new ArrayList<>();
        defaultRootNames.add("Uncategorized");
        defaultRootNames.add("Income");
        defaultRootNames.add("Expenses");

        // create root categories
        for (String name:defaultRootNames) {
            dbManager.createCategory(new Category(newAccount.getId(), 0, name));
        }

        // list of default subcategory names
        ArrayList<Category> defaultCategories = new ArrayList<>();
        defaultCategories.add(new Category(newAccount.getId(), INCOME, "Gift"));
        defaultCategories.add(new Category(newAccount.getId(), EXPENSES, "Household"));
        defaultCategories.add(new Category(newAccount.getId(), EXPENSES, "Sports"));
        defaultCategories.add(new Category(newAccount.getId(), EXPENSES, "Food and drinks"));

        // create default subcategories
        for (Category category: defaultCategories) {
            dbManager.createCategory(category);
        }
    }
}
