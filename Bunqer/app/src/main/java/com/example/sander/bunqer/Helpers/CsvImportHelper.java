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
import java.util.ArrayList;

public class CsvImportHelper {

    private static DBManager dbManager;

    // constructor
    public CsvImportHelper() {
    }

    /**
     * Processes the CSV file that is shared through a intent.action.SEND. Returns
     * an ArrayList of Transaction objects.
     *
     * @param context
     * @param receivedIntent
     * @return
     */
    public static ArrayList<Transaction> getTransactionList(Context context, Intent receivedIntent) {
        ArrayList<Transaction> transactions = null;
        dbManager = DBManager.getInstance();

        Uri uri = receivedIntent.getClipData().getItemAt(0).getUri();
        if (uri != null) {
            try {
                // get buffered reader object to read the csv line by line
                BufferedReader bReader = getBufferedReader(context, uri);

                // skip over first line
                bReader.readLine();

                // get list with transactions
                transactions = createTransactionFromCsv(bReader);

                // categorize and return transactions
                return CategoryHelper.categorize(transactions);
            } catch (NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }

        // shouldn't get here but, just to be safe
        Log.e("getTransactionList", "Failed to import transactions");
        return transactions;
    }

    /**
     * Returns transaction list with transactions created from CSV.
     *
     * @param bReader
     * @return
     * @throws IOException
     */
    private static ArrayList<Transaction> createTransactionFromCsv(BufferedReader bReader)
            throws IOException {

        ArrayList<Transaction> transactions = new ArrayList<>();

        String line;
        while ((line = bReader.readLine()) != null) {
            // general formatting and splitting of line
            line = line.replace("\"", "");
            String[] rowData = line.split(";");

            // create new transaction using the data
            Transaction transaction = new Transaction();
            transaction.setDate(rowData[0]);
            transaction.setAmount(rowData[1]);
            transaction.setAccount(rowData[2]);
            transaction.setCounterpartyAccount(rowData[3]);
            transaction.setCounterpartyName(rowData[4]);
            transaction.setDescription(rowData[5].replaceAll("[^a-zA-Z\\d\\s]", ""));
            transaction.setAccountId(getAccountId(transaction));

            // don't add if identical transaction already exists
            if (transaction.isNotDuplicate()) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    /**
     * Returns a BufferedReader to read the contents of the CSV line by line.
     *
     * @param context
     * @param uri
     * @return
     * @throws IOException
     */
    private static BufferedReader getBufferedReader(Context context, Uri uri) throws IOException {
        AssetFileDescriptor descriptor = context.getContentResolver()
                .openTypedAssetFileDescriptor(uri, "text/*", null);
        if (descriptor == null) {
            Log.e("error", "descriptor is null");
        }
        FileInputStream inputStream = descriptor.createInputStream();
        InputStreamReader isReader = new InputStreamReader(inputStream, "UTF-8");

        return new BufferedReader(isReader);
    }

    /**
     * Checks if the account associated with the transaction already exists in the DB.
     *
     * @param transaction
     * @return
     */
    private static int getAccountId(Transaction transaction) {
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

    /**
     * Sets up new account in DB if the account associated with the transaction doesn't exist yet.
     * Also makes a call to set up the default categories for the new account.
     *
     * @param transaction
     * @return
     */
    private static Account setupAccount(Transaction transaction) {
        // create account
        Account account = new Account(transaction.getAccount(), transaction.getAccount());
        dbManager.createAccount(account);

        // get new account from database
        ArrayList<Account> accounts = dbManager.readAccounts();
        Account newAccount = accounts.get(accounts.size()-1);

        CategoryHelper.setupDefaultCategories(newAccount);

        // return an instance of the new account
        return accounts.get(accounts.size()-1);
    }
}
