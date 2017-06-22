package com.example.sander.bunqer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.Helpers.CategoryHelper;
import com.example.sander.bunqer.Helpers.ChartHelper;
import com.example.sander.bunqer.ModelClasses.Category;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;


public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private ChartHelper mChartHelper;
    private PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // get chart and charthelper
        mPieChart = (PieChart) findViewById(R.id.monthChart);
        mChartHelper = new ChartHelper(getApplicationContext(), this);

        buildChart();
    }

    private void buildChart() {
        PieData data;

        // if user got here from a category specific transaction list
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getSerializable("category") != null) {
            // get the category
            Category category = (Category) getIntent().getExtras().getSerializable("category");

            // get fresh copy of its parent's subcategories, if any
            if (category.getParentId() != CategoryHelper.ROOT) {
                Category freshCategory = DBManager.getInstance().readCategories(category.getParentId()).get(0);
                data = mChartHelper.setupPieData(freshCategory.getSubcategories());
            }
            else {
                data = getAllCategories();
            }
        }
        else {
            data = getAllCategories();
        }

        setChart(data);
    }

    private void setChart(PieData data) {
        mPieChart.setData(data);
        mPieChart.setTouchEnabled(true);
        mPieChart.setOnChartValueSelectedListener(this);
        mPieChart.invalidate();
    }

    private PieData getAllCategories() {
        ArrayList<Category> categories = DBManager.getInstance().readCategories(null);
        PieData data = mChartHelper.setupPieData(categories);
        return data;
    }

    @Override
    public void onValueSelected(Entry entry, Highlight h) {
        PieDataSet newSet = mChartHelper.rebuildDataset((PieEntry) entry, mPieChart);

        if (newSet != null) {
            // change datasets
            mPieChart.getData().removeDataSet(0);
            mPieChart.getData().setDataSet(newSet);

            // update chart with new dataset
            mPieChart.getData().notifyDataChanged();
            mPieChart.notifyDataSetChanged();
            mPieChart.invalidate();
        }
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    public void onBackPressed() {

        // if there are parent categories, render their chart
        Category category = (Category) mPieChart.getData().getDataSet().getEntryForIndex(0).getData();
        ArrayList<Category> parentCategories = new ArrayList<>();

        // check if there are parent categories
        if (category.getParentId() > 0) {
            // if there are, go get it and add it to list
            Category parentCategory = DBManager.getInstance().readCategories(category.getParentId()).get(0);
            for (Category cat:DBManager.getInstance().readCategories(null))
                if (parentCategory.getParentId() == cat.getParentId()) {
                    parentCategories.add(cat);
                }

            // set up the chart with the parent categories
            PieData data = mChartHelper.setupPieData(parentCategories);
            setChart(data);
        }

        // otherwise exit the app
        else {
            super.onBackPressed();
        }
    }

    // to prevent double instances of the activity
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        buildChart();
    }
}