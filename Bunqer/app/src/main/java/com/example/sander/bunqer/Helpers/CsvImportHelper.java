package com.example.sander.bunqer.Helpers;
/*
 * Created by sander on 11-6-17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.util.Log;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Account;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class CsvImportHelper {

    private static DBManager dbManager;

    // constructor
    public CsvImportHelper() {
    }

    /**
     * A method that processes the CSV file that is shared through a intent.action.SEND. It returns
     * an ArrayList of Transaction objects.
     *
     * @param context
     * @param receivedIntent
     * @return
     */
    public static ArrayList<Transaction> getTransactionList(Context context, Intent receivedIntent) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        dbManager = DBManager.getInstance(context);

        Uri uri = receivedIntent.getClipData().getItemAt(0).getUri();
        if (uri != null) {
            FileInputStream inputStream;
            try {
                AssetFileDescriptor descriptor = context.getContentResolver()
                        .openTypedAssetFileDescriptor(uri, "text/*", null);
                inputStream = descriptor.createInputStream();
                InputStreamReader isReader = new InputStreamReader(inputStream, "UTF-8");

                BufferedReader bReader = new BufferedReader(isReader);

                // skip over first line
                bReader.readLine();

                String line;
                while ((line = bReader.readLine()) != null) {
                    line = line.replace("\"", "");
                    String[] rowData = line.split(";");
                    Transaction transaction = new Transaction();
                    transaction.setDate(rowData[0]);
                    transaction.setAmount(rowData[1]);
                    transaction.setAccount(rowData[2]);
                    transaction.setCounterparty_account(rowData[3]);
                    transaction.setCounterparty_name(rowData[4]);
                    transaction.setDescription(rowData[5]);
                    transaction.setAccount_id(getAccount_id(transaction));
                    transactions.add(transaction);
                }

                // categorize transactions
                Log.d("log","completing import");
                return CategoryHelper.getInstance(context).categorize(transactions);

            } catch (NullPointerException e) {
                Log.w("ClipData", "Failure to create stream");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // shouldn't get here but, just to be safe
        Log.e("getTransactionList", "Failed to import transactions");
        return transactions;
    }

    private static int getAccount_id(Transaction transaction) {
        // check if account already exists
        ArrayList<Account> accounts = dbManager.readAccounts();
        for (Account account: accounts) {
            if (account.getNumber().equals(transaction.getAccount())) {
                // and brand transaction with the account id
                return account.getId();
            }
        }

        // otherwise setup new account
        Account newAccount = setupAccount(transaction);
        return newAccount.getId();
    }

    private static Account setupAccount(Transaction transaction) {
        // create account
        Account account = new Account(transaction.getAccount(), transaction.getAccount());
        dbManager.createAccount(account);

        // get new account from database
        ArrayList<Account> accounts = dbManager.readAccounts();
        Account newAccount = accounts.get(accounts.size()-1);

        setupDefaultCategories(newAccount);

        return accounts.get(accounts.size()-1);
    }

    private static void setupDefaultCategories(Account newAccount) {
        // empty list of categories
        ArrayList<Category> defaultCategories = new ArrayList<>();

        // TODO: 13-6-17 Improve default categories
        defaultCategories.add(new Category(newAccount.getId(), "Uncategorized"));
        defaultCategories.add(new Category(newAccount.getId(), "Income"));
        defaultCategories.add(new Category(newAccount.getId(), "Household"));
        defaultCategories.add(new Category(newAccount.getId(), "Sports"));
        defaultCategories.add(new Category(newAccount.getId(), "Eating out"));

        for (Category category: defaultCategories) {
            dbManager.createCategory(category);
        }
    }
}
