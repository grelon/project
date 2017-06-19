package com.example.sander.bunqer.Helpers;
/*
 * Created by sander on 13-6-17.
 *
 * prepares data for charts
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.TransactionListActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;

public class ChartHelper {
    private ArrayList<Category> categories;
    private DBManager dbManager;
    private Context context;

    public ChartHelper(Context context) {
        dbManager = DBManager.getInstance();
        this.context = context;
    }

    public PieData setupPieData() {
        categories = dbManager.readCategories(null);

        // assert if there is data
        if (categories.size() > 0) {
            List<PieEntry> entries = new ArrayList<>();

            ArrayList<Category> incomeCategories = new ArrayList<>();
            ArrayList<Category> expensesCategories = new ArrayList<>();

            int total = 0;

            // calculate absolute total of all categories
            for (Category category:categories) {
                if (category.getParentId() == 0) {
                    total += (abs(category.getTotalValue()));
                }
            }

            // calculate percentages of total per category and produce PieEntries
            for (Category category:categories) {
                float percentage = (abs(category.getTotalValue()) * 100.0f) / total;

                entries.add(new PieEntry(percentage, category.getName(), ))

                if (category.getId() == CategoryHelper.UNCATEGORIZED) {
                    incomeTotal += category.getTotalValue();
                    incomeCategories.add(category);
                }
                else {
                    expensesTotal += category.getTotalValue();
                    expensesCategories.add(category);
                }
            }

            // calculate percentages for chart
            expensesTotal = -expensesTotal;
            int total = incomeTotal + expensesTotal;
            float mIncomePercentage = (incomeTotal*100.0f) / total;
            float mExpensesPercentage = (expensesTotal*100.0f) / total;

            entries.add(new PieEntry(mIncomePercentage, "Income", incomeCategories));
            entries.add(new PieEntry(mExpensesPercentage, "Expenses", expensesCategories));

            PieDataSet set = new PieDataSet(entries, "Total");
            set.setSliceSpace(2f);
            set.setSelectionShift(0f);

            ArrayList<Integer> colors = new ArrayList<>();
            // green
            colors.add(ColorTemplate.rgb("#008000"));
            // red
            colors.add(ColorTemplate.rgb("#ff0000"));
            set.setColors(colors);

            return new PieData(set);
        }
        return null;
    }

    public PieDataSet rebuildDataset(PieEntry selectedEntry, PieChart pieChart) {
        ArrayList<Category> currentCategories = new ArrayList<>();
        int currentCategoriesTotal = 0;

        // when expenses has been selected
        if (Objects.equals("Expenses", selectedEntry.getLabel())) {
            // get all entries from dataset and remove them
            List<PieEntry> entries = pieChart.getData().getDataSet().getEntriesForXValue(0);
            entries.clear();

            // add all categories in entry
            for (Category category:(ArrayList<Category>) selectedEntry.getData()) {
                int totalValue = category.getTotalValue();
                if (totalValue != 0) {
                    currentCategories.add(category);
                    currentCategoriesTotal += totalValue;
                }
            }

            // add entries to list
            for (Category category:currentCategories) {
                entries.add(new PieEntry(
                        (float)category.getTotalValue() / currentCategoriesTotal*100,
                        category.getName(), category));
            }

            PieDataSet set = new PieDataSet(entries, "Expenses");
            set.setSliceSpace(2f);
            set.setSelectionShift(0f);
            set.setColors(ColorTemplate.MATERIAL_COLORS);

            return set;
        }
        // when income has been selected
        else if (Objects.equals("Income", selectedEntry.getLabel())) {
            Log.d("log", "income selected");

            // get all entries from dataset and remove them
            List<PieEntry> entries = pieChart.getData().getDataSet().getEntriesForXValue(0);
            entries.clear();

            // add all categories in entry
            for (Category category:(ArrayList<Category>) selectedEntry.getData()) {
                int totalValue = category.getTotalValue();
                if (totalValue != 0) {
                    currentCategories.add(category);
                    currentCategoriesTotal += totalValue;
                }
            }

            // add entries to list
            for (Category category:currentCategories) {
                entries.add(new PieEntry(
                        (float)category.getTotalValue() / currentCategoriesTotal*100,
                        category.getName(), category));
            }

            PieDataSet set = new PieDataSet(entries, "Income");
            set.setSliceSpace(2f);
            set.setSelectionShift(0f);
            set.setColors(ColorTemplate.MATERIAL_COLORS);

            return set;
        }

        // when a category has been selected
        else {
            Category category = (Category) selectedEntry.getData();
            Intent toTransactionList = new Intent(context, TransactionListActivity.class);
            toTransactionList.putExtra("category", category);
            context.startActivity(toTransactionList);
        }
    return null;
    }
}
