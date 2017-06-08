package com.example.sander.bunqer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        // setup AppBar
        getSupportActionBar().setTitle("Expenses");

        // chart
        PieChart pieChart = (PieChart) findViewById(R.id.catChart);

        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(30f, "Groceries"));
        entries.add(new PieEntry(55f, "Rent"));
        entries.add(new PieEntry(15f, "Eating out"));

        PieDataSet set = new PieDataSet(entries, "");
        set.setSliceSpace(2f);
        set.setSelectionShift(0f);

        set.setColors(ColorTemplate.PASTEL_COLORS);
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.setTouchEnabled(true);
        pieChart.setOnChartValueSelectedListener(this);
        pieChart.invalidate();
        // end of chart
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        // hardcoded intent towards SubCategoriesActivity
        Intent toSubCategoriesIntent = new Intent(getApplicationContext(), SubCategoriesActivity.class);
        startActivity(toSubCategoriesIntent);
    }

    @Override
    public void onNothingSelected() {

    }
}
