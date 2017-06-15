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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChartHelper {
    private ArrayList<Transaction> transactions;
    private ArrayList<Category> categories;
    private DBManager dbManager;
    private float mExpensesPercentage;
    private float mIncomePercentage;
    private Context context;

    public ChartHelper(Context context) {
        dbManager = DBManager.getInstance(context);
        this.context = context;
    }

    public PieData setupPieData() {
        transactions = dbManager.readTransactions();
        categories = dbManager.readCategories();

        // assert if there is data
        if (categories.size() > 0) {
            List<PieEntry> entries = new ArrayList<>();

            float incomeTotal = 0.00f;
            float expensesTotal = 0.00f;
            for (Transaction transaction:transactions) {
                if (transaction.getCategoryId() == 2) {
                    incomeTotal += transaction.getAmount();
                }
                else {
                    expensesTotal += transaction.getAmount();
                }
            }

            expensesTotal = -expensesTotal;

            float total = incomeTotal + expensesTotal;
            mIncomePercentage = (incomeTotal/total*100f);
            mExpensesPercentage = (expensesTotal/total*100f);

            entries.add(new PieEntry(mIncomePercentage, categories.get(1).getName()));
            entries.add(new PieEntry(mExpensesPercentage, "Expenses"));

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

    public PieDataSet rebuildData(PieEntry selectedEntry, PieChart pieChart) {
        ArrayList<Category> currentCategories = new ArrayList<>();
        int currentCategoriesTotal = 0;

        // when expenses has been selected
        if (mExpensesPercentage == selectedEntry.getY()) {
            // get all entries from dataset and remove them
            List<PieEntry> entries = pieChart.getData().getDataSet().getEntriesForXValue(0);
            entries.clear();

            // add all categories, except income, to current categories
            for (Category category:categories) {
                if (category.getId() != 2) {
                    category.updateTransactions(context);
                    currentCategoriesTotal += category.getTotalValue();
                    if (category.getTotalValue() < 0) {
                        currentCategories.add(category);
                    }
                }
            }

            // add entries to list
            for (Category category:currentCategories) {
                PieEntry entry = new PieEntry((float)category.getTotalValue()/currentCategoriesTotal*100, category.getName(), category);
                entries.add(entry);
            }

            PieDataSet set = new PieDataSet(entries, "Expenses");
            set.setSliceSpace(2f);
            set.setSelectionShift(0f);
            set.setColors(ColorTemplate.MATERIAL_COLORS);

            return set;
        }
        // when income has been selected
        else if (mIncomePercentage == selectedEntry.getY()) {
            Log.d("log", "income selected");
            return null;
        }

        // when a category has been selected
        else {
            Category selectedCategory = null;
            for (Category category:categories) {
                if (Objects.equals(category.getName(), selectedEntry.getLabel())) {
                    selectedCategory = category;
                    Log.d("log", selectedCategory.getName() +" has been selected");
                }
            }
            return null;
        }
    }
}
