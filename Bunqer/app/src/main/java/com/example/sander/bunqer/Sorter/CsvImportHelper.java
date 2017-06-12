package com.example.sander.bunqer.Sorter;
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
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvImportHelper {

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

        ArrayList<Account> accounts = DBManager.getInstance(context).readAccounts();

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


                    String[] rowData = line.split(";");
                    Transaction transaction = new Transaction();
                    transaction.setDate(rowData[0]);
                    transaction.setAmount(rowData[1]);
                    transaction.setAccount(rowData[2]);
                    transaction.setAccount_id(getAccount_id(accounts, rowData[2]));
                    transaction.setCounterparty_account(rowData[3]);
                    transaction.setCounterparty_name(rowData[4]);
                    transaction.setDescription(rowData[5]);
                    transactions.add(transaction);
                }
                return transactions;

            } catch (NullPointerException e) {
                Log.w("ClipData", "Failure to create stream");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // shouldn't get here but, just to be safe
        Log.w("getTransactionList", "URI is null");
        return transactions;
    }

    private static int getAccount_id(ArrayList<Account> accounts, String account_number) {
        // check if account already exists
        for (Account account: accounts) {
            if (account.getNumber().equals(account_number)) {
                // and brand transaction with the account id
                return account.getId();
            }
        }

        // otherwise brand transaction with 0, to signify it is not assigned to an account
        return 0;
    }
}
