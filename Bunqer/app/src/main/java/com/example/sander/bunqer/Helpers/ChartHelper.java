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

            int total = 0;

            // calculate absolute total of all categories
            for (Category category:categories) {
                total += (abs(category.getTotalValue()));
            }

            // calculate percentages of total per category and add PieEntries
            for (Category category:categories) {
                float percentage = (abs(category.getTotalValue()) * 100.0f) / total;
                entries.add(new PieEntry(percentage, category.getName(), category));
            }

            PieDataSet set = new PieDataSet(entries, "Total");
            set.setSliceSpace(2f);
            set.setSelectionShift(0f);

            ArrayList<Integer> colors = new ArrayList<>();
            // green
            colors.add(ColorTemplate.rgb("#008000"));
            // red
            colors.add(ColorTemplate.rgb("#ff0000"));
            // yellow
            colors.add(ColorTemplate.rgb("#FFFF00"));
            set.setColors(colors);

            return new PieData(set);
        }
        return null;
    }

    public PieDataSet rebuildDataset(PieEntry selectedEntry, PieChart pieChart) {
        ArrayList<Category> newCategories = new ArrayList<>();
        int newCategoriesTotal = 0;

        Category category = (Category) selectedEntry.getData();

        // if category has subcategories, build a new chart
        if (category.getSubcategories().size() != 0) {
            // get all entries from dataset and remove them
            List<PieEntry> entries = pieChart.getData().getDataSet().getEntriesForXValue(0);
            entries.clear();

            // add all categories in entry
            for (Category subCategory:category.getSubcategories()) {
                int totalValue = subCategory.getTotalValue();
                if (totalValue != 0) {
                    newCategories.add(subCategory);
                    newCategoriesTotal += totalValue;
                }
            }

            // add entries to list
            for (Category newCategory:newCategories) {
                entries.add(new PieEntry(
                        (float)category.getTotalValue() / newCategoriesTotal*100,
                        newCategory.getName(), newCategory));
            }

            PieDataSet set = new PieDataSet(entries, "Expenses");
            set.setSliceSpace(2f);
            set.setSelectionShift(0f);
            set.setColors(ColorTemplate.MATERIAL_COLORS);

            return set;
        }

        // otherwise send to Transaction List of category
        else {
            Intent toTransactionList = new Intent(context, TransactionListActivity.class);
            toTransactionList.putExtra("category", category);
            context.startActivity(toTransactionList);
        }

        // shouldn't be able to get here
        return null;
    }
}
