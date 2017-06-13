package com.example.sander.bunqer.Sorter;
/*
 * Created by sander on 12-6-17.
 *
 * Sorts transactions into categories.
 */


import android.content.Context;
import android.util.Log;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class CategoryHelper {
    private static DBManager dbManager;

    public CategoryHelper(Context context) {
        this.dbManager = DBManager.getInstance(context);
    }

    public void categorize(ArrayList<Transaction> transactions) {
        Log.d("log", "start categorize()");
    }
}
