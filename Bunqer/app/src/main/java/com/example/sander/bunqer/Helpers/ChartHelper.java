package com.example.sander.bunqer.Helpers;
/*
 * Created by sander on 13-6-17.
 *
 * prepares data for charts
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
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
        categories = dbManager.readCategories();

        // assert if there is data
        if (categories.size() > 0 && transactions.size() > 0) {
            List<PieEntry> entries = new ArrayList<>();

            float incomeTotal = 0.00f;
            float expensesTotal = 0.00f;
            for (Transaction transaction:transactions) {
                if (transaction.getCategory_id() == 2) {
                    incomeTotal += transaction.getAmount();
                }
                else {
                    expensesTotal += transaction.getAmount();
                }
            }

            expensesTotal = -expensesTotal;

            float total = incomeTotal + expensesTotal;
            float incomeTotalPercentage = (incomeTotal/total*100f);
            float expensesTotalPercentage = (expensesTotal/total*100f);

            entries.add(new PieEntry(incomeTotalPercentage, categories.get(1).getName()));
            entries.add(new PieEntry(expensesTotalPercentage, "Expenses"));

            PieDataSet set = new PieDataSet(entries, "Total");
            set.setSliceSpace(2f);
            set.setSelectionShift(0f);

            ArrayList<Integer> colors = new ArrayList<>();
            // green
            colors.add(ColorTemplate.rgb("#008000"));
            // red
            colors.add(ColorTemplate.rgb("#ff0000"));

            set.setColors(colors);
            PieData data = new PieData(set);

            return data;
        }
        return null;
    }

    public PieDataSet rebuildData(Entry selectedEntry, PieChart pieChart) {
        Category selectedCategory;
        for (Category category:categories) {
            if (category.getCurrentChartPercentage() == selectedEntry.getX()) {
                selectedCategory = category;
            }
        }
        return null;
    }
}
