package com.example.sander.bunqer;
/*
 * Created by sander on 11-6-17.
 */

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CsvImportHelper {

    public CsvImportHelper(Context context) {
        Context context = context;
    }

    public static ArrayList<Transaction> getTransactionList(Intent receivedIntent) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        Uri uri = receivedIntent.getClipData().getItemAt(0).getUri();
        if (uri != null) {
            Log.d("log", "uri is not null");
            FileInputStream inputStream;
            try {
                Log.d("log", "try 1");
                AssetFileDescriptor descriptor = getContentResolver()
                        .openTypedAssetFileDescriptor(uri, "text/*", null);
                Log.d("log", "try 2");
                inputStream = descriptor.createInputStream();
                Log.d("log", "try 3");
                InputStreamReader isReader = new InputStreamReader(inputStream, "UTF-8");
                Log.d("log", "try 4");

                ArrayList<String> lineArray = new ArrayList<>();

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
                    transaction.setCounterparty(rowData[3]);
                    transaction.setName(rowData[4]);
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
}
