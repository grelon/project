package com.example.sander.bunqer.Helpers;
/*
 * Created by sander on 13-6-17.
 *
 * prepares data for charts
 */

import android.app.Activity;
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
    private Context context;
    private Activity activity;

    public ChartHelper(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public PieData setupPieData(ArrayList<Category> cats) {
        categories = cats;
        // assert if there is data
        if (categories.size() > 0) {
            List<PieEntry> entries = new ArrayList<>();

            int total = 0;

            // create list of categories that have a total value higher than 0
            ArrayList<Category> usedCategories = new ArrayList<>();

            // calculate absolute total of all categories
            for (Category category:categories) {

                // add category to list if its total value is not 0
                if (category.getTotalValue() != 0) {
                    usedCategories.add(category);
                    total += (abs(category.getTotalValue()));
                }
            }


            // calculate percentages of total per category and add PieEntries
            for (Category category:usedCategories) {
                float percentage = (abs(category.getTotalValue()) * 100.0f) / total;
                entries.add(new PieEntry(percentage, category.getName(), category));
            }

            PieDataSet set = new PieDataSet(entries, "Total");
            set.setSliceSpace(2f);
            set.setSelectionShift(0f);

            // set default colours
            set.setColors(ColorTemplate.MATERIAL_COLORS);

            // update chart label to parent name if it exists
            if (usedCategories.get(0).getParentId() != CategoryHelper.ROOT) {
                set.setLabel(DBManager.getInstance()
                        .readCategories(usedCategories.get(0).getParentId()).get(0).getName());
            }

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

                // if the total value of a category is 0, don't include the category
                if (totalValue != 0) {
                    newCategories.add(subCategory);
                    newCategoriesTotal += totalValue;
                }
            }

            // add entries to list
            for (Category newCategory:newCategories) {
                entries.add(new PieEntry(
                        (float)newCategory.getTotalValue() / newCategoriesTotal*100,
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
            activity.finish();
        }

        // shouldn't be able to get here
        return null;
    }
}
