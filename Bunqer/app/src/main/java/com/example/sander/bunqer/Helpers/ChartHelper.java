package com.example.sander.bunqer.Helpers;
/*
 * Created by sander on 13-6-17.
 *
 * prepares data for charts
 */

import android.content.Context;
import android.util.Log;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class ChartHelper {
    ArrayList<Transaction> transactions;
    ArrayList<Category> categories;
    DBManager dbManager;

    public ChartHelper(Context context) {
        dbManager = DBManager.getInstance(context);
    }

    public PieData setupPieData() {
        transactions = dbManager.readTransactions();
        Log.d("log", "all transactions: \n" + transactions.toString());

        categories = dbManager.readCategories();

        List<PieEntry> entries = new ArrayList<>();

        float incomeTotal = 0.00f;
        float expensesTotal = 0.00f;
        for (Transaction transaction:transactions) {
            if (transaction.getCategory_id() == 1) {
                incomeTotal += transaction.getAmount();
            }
            else if (transaction.getCategory_id() == 2){
                expensesTotal += transaction.getAmount();
            }
        }
        expensesTotal = -expensesTotal;

        float total = incomeTotal + expensesTotal;
        float incomeTotalPercentage = (incomeTotal/total*100f);
        float expensesTotalPercentage = (expensesTotal/total*100f);



        entries.add(new PieEntry(incomeTotalPercentage, categories.get(0).getName()));
        entries.add(new PieEntry(expensesTotalPercentage, categories.get(1).getName()));

        PieDataSet set = new PieDataSet(entries, "Total");
        set.setSliceSpace(2f);
        set.setSelectionShift(0f);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ColorTemplate.rgb("#008000"));
        colors.add(ColorTemplate.rgb("#ff0000"));

        set.setColors(colors);
        PieData data = new PieData(set);

        return data;
    }
}
