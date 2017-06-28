package com.example.sander.bunqer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.Helpers.CategoryHelper;
import com.example.sander.bunqer.Helpers.ChartHelper;
import com.example.sander.bunqer.Helpers.CsvImportHelper;
import com.example.sander.bunqer.Helpers.CurrencyFormatter;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private static final String NO_DATA_TEXT = "Sorry, no transactions found. Upload your latest transactions from the Bunq app.";
    private ChartHelper mChartHelper;
    private PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // when new transactions are shared, import them first
        importNew();


        // get chart charthelper
        mPieChart = (PieChart) findViewById(R.id.monthChart);
        mChartHelper = new ChartHelper(getApplicationContext(), this);

        buildChart();
    }

    private void importNew() throws NullPointerException {
        try {
            if (getIntent().getAction().equals("android.intent.action.SEND") &&
                    getIntent().normalizeMimeType(getIntent().getType()).equals("text/csv")) {
                // import new transactions and return them in a transaction list
                ArrayList<Transaction> transactions = CsvImportHelper.getTransactionList(getApplicationContext(), getIntent());

                // notify users of number new transactions
                if (transactions.size() > 1) {
                    Toast.makeText(this, transactions.size() + " new transactions added.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException e) {
            // apparently there is no data to import, so just move on.
        }
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
        String label = "";

        if (data != null) {
            // set currency formatter if data isn't null
            data.setValueFormatter(new CurrencyFormatter());

            // font settings for all values of Datasets in the chart
            data.setValueTextColor(ColorTemplate.rgb("#ffffff"));
            data.setValueTextSize(16);
            label = data.getDataSet().getLabel();
        }

        // populate chart with data
        mPieChart.setData(data);

        // remove description from chart
        Description description = new Description();
        description.setText("");
        mPieChart.setDescription(description);

        // set text when no data is available
        mPieChart.setNoDataText(NO_DATA_TEXT);
        mPieChart.setNoDataTextColor(R.color.colorPrimaryDark);

        // set centertext to label of current dataset
        mPieChart.setCenterText(label);

        // format legend
        mChartHelper.formatLegend(mPieChart);

        // enable user interaction
        mPieChart.setTouchEnabled(true);
        mPieChart.setOnChartValueSelectedListener(this);

        // refresh chart to enable any changes made
        mPieChart.invalidate();
    }

    private PieData getAllCategories() {
        ArrayList<Category> categories = DBManager.getInstance().readCategories(null);
        PieData data = mChartHelper.setupPieData(categories);
        return data;
    }

    @Override
    public void onValueSelected(Entry entry, Highlight h) {
        PieData newData = mChartHelper.rebuildPieData((PieEntry) entry, mPieChart);

        if (newData != null) {
            // change datasets
            mPieChart.getData().removeDataSet(0);
            setChart(newData);
        }
    }

    @Override
    public void onNothingSelected() {
    }

    @Override
    public void onBackPressed() {

        try {
            // if there are parent categories, render their chart
            Category category = (Category) mPieChart.getData().getDataSet().getEntryForIndex(0).getData();
            ArrayList<Category> parentCategories = new ArrayList<>();

            // check if there are parent categories
            if (category.getParentId() > 0) {
                // if there are, go get it and add it to list
                Category parentCategory = DBManager.getInstance().readCategories(category.getParentId()).get(0);
                for (Category cat : DBManager.getInstance().readCategories(null))
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
        } catch (NullPointerException e) {
            Log.d("log", "data is null");
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