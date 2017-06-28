package com.example.sander.bunqer;
/*
 * Created by sander on 15-6-17.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * Controls the action on the chart screen.
 */

public class ChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private static final String NO_DATA_TEXT =
            "Sorry, no transactions found. Upload your latest transactions from the Bunq app.";
    private ChartHelper mChartHelper;
    private PieChart mPieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        // when new transactions are shared, try to import them first
        importNew();

        mPieChart = (PieChart) findViewById(R.id.monthChart);
        mChartHelper = new ChartHelper(getApplicationContext(), this);

        PieData data = dataResolver();
        setChart(data);
    }

    /**
     * Tries to import new transactions.
     *
     * @throws NullPointerException
     */
    private void importNew() throws NullPointerException {
        try {
            // check if activity is started by sharing a CSV file
            if (getIntent().getAction().equals("android.intent.action.SEND") &&
                    getIntent().normalizeMimeType(getIntent().getType()).equals("text/csv")) {

                // import new transactions and return them in a transaction list
                ArrayList<Transaction> transactions =
                        CsvImportHelper.getTransactionList(getApplicationContext(), getIntent());

                // notify users of number new transactions
                if (transactions.size() > 1) {
                    Toast.makeText(this, transactions.size() + " new transactions added."
                            , Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException e) {
            // there is no data to import, but no worries, the user will be notified by the chart object
        }
    }

    /**
     * Resolves what content should go into the chart.
     */
    private PieData dataResolver() {
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

            // if it doesn't have any parents, use the root categories.
            else {
                data = getRootCategories();
            }
        }

        // if user got here without asking for a specific category, get root categories.
        else {
            data = getRootCategories();
        }

        return data;
    }

    /**
     * Populates chart with actual data and also prettifies it a bit.
     * @param data
     */
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

        mPieChart.setNoDataText(NO_DATA_TEXT);
        mPieChart.setNoDataTextColor(R.color.colorPrimaryDark);

        mPieChart.setCenterText(label);
        mChartHelper.formatLegend(mPieChart);

        mPieChart.setTouchEnabled(true);
        mPieChart.setOnChartValueSelectedListener(this);

        // refresh chart to enable any changes made
        mPieChart.invalidate();
    }

    /**
     * Gets a PieData object filled with data from the root categories.
     * @return
     */
    private PieData getRootCategories() {
        ArrayList<Category> categories = DBManager.getInstance().readCategories(null);
        PieData data = mChartHelper.setupPieData(categories);
        return data;
    }

    @Override
    public void onValueSelected(Entry entry, Highlight h) {
        // use selected entry to build a new PieData object.
        PieData newData = mChartHelper.rebuildPieData((PieEntry) entry, mPieChart);

        if (newData != null) {
            // change datasets
            mPieChart.getData().removeDataSet(0);
            setChart(newData);
        }
    }

    @Override
    public void onNothingSelected() {
        // do nothing
    }

    @Override
    public void onBackPressed() {
        // try to render chart of parent categories, if any exist.
        try {
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
            // means data is null (i.e. there are no transactions) so just exit the app.
            super.onBackPressed();
        }
    }

    /**
     * Prevents double instances of the activity.
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        dataResolver();
    }
}