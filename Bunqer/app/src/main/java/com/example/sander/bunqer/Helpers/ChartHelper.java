package com.example.sander.bunqer.Helpers;
/*
 * Created by sander on 13-6-17.
 *
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.TransactionListActivity;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.abs;

/**
 * Helps chart object to build with several methods.
 */

public class ChartHelper {
    private Context context;
    private Activity activity;

    public ChartHelper(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * Turns a list of categories into a PieData object
     * @param cats
     * @return
     */
    public PieData setupPieData(ArrayList<Category> cats) {
        ArrayList<Category> categories = cats;
        // assert if there is data
        if (categories.size() > 0) {
            List<PieEntry> entries = new ArrayList<>();

            // make list of categories that have a value other than 0
            ArrayList<Category> usedCategories = new ArrayList<>();
            for (Category category: categories) {
                if (category.getTotalValue() != 0) {
                    usedCategories.add(category);
                }
            }

            // create PieEntries out of categories and add them to the list
            for (Category category:usedCategories) {
                entries.add(new PieEntry(abs(category.getTotalValue()), category.getName(), category));
            }

            PieDataSet set = new PieDataSet(entries, "Total");
            set = prettifySet(set);

            // update chart label to parent name if it exists
            if (usedCategories.get(0).getParentId() != CategoryHelper.ROOT) {
                set.setLabel(DBManager.getInstance()
                        .readCategories(usedCategories.get(0).getParentId()).get(0).getName());
            }

            return new PieData(set);
        }
        return null;
    }

    /**
     * Rebuilds an existing PieData object with different data. Uses an existing chart and selected
     * entry to figure out with what data the object needs to be build.
     *
     * @param selectedEntry
     * @param pieChart
     * @return
     */
    public PieData rebuildPieData(PieEntry selectedEntry, PieChart pieChart) {
        ArrayList<Category> newCategories = new ArrayList<>();

        Category category = (Category) selectedEntry.getData();

        // if category has subcategories, build a new chart
        if (category.getSubcategories().size() != 0) {

            // get all entries from dataset and remove them
            List<PieEntry> entries = pieChart.getData().getDataSet().getEntriesForXValue(0);
            entries.clear();

            // add all categories in entry
            for (Category subCategory:category.getSubcategories()) {
                int subCategoryTotalValueValue = subCategory.getTotalValue();

                // if the total value of a category is 0, don't include the category
                if (subCategoryTotalValueValue != 0) {
                    newCategories.add(subCategory);
                }
            }

            // add entries to list
            for (Category newCategory:newCategories) {
                entries.add(new PieEntry(
                        (float) abs(newCategory.getTotalValue()),
                        newCategory.getName(), newCategory));
            }

            PieDataSet set = new PieDataSet(entries, selectedEntry.getLabel());
            set = prettifySet(set);

            return new PieData(set);
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

    /**
     * Prettifies dataset
     *
     * @param set
     * @return
     */
    private PieDataSet prettifySet(PieDataSet set) {
        set.setSliceSpace(2f);
        set.setSelectionShift(0f);
        set.setColors(ColorTemplate.JOYFUL_COLORS);
        return set;
    }

    /**
     * Prettifies legend.
     *
     * @param mPieChart
     */
    public void formatLegend(PieChart mPieChart) {
        Legend legend = mPieChart.getLegend();
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setYOffset(25);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(12);
    }
}
