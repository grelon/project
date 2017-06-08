package com.example.sander.bunqer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

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


public class MonthActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private float selectedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month);

        // setup AppBar
        getSupportActionBar().setTitle("Month");

        // chart
        PieChart pieChart = (PieChart) findViewById(R.id.monthChart);

        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(55f, "Income"));
        entries.add(new PieEntry(45f, "Expenses"));

        PieDataSet set = new PieDataSet(entries, "Total");
        set.setSliceSpace(2f);
        set.setSelectionShift(0f);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ColorTemplate.rgb("#008000"));
        colors.add(ColorTemplate.rgb("#ff0000"));

        set.setColors(colors);
        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.setTouchEnabled(true);
        pieChart.setOnChartValueSelectedListener(this);
        pieChart.invalidate();
        // end of chart
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        // hardcoded intent towards CategoriesActivity
        Intent toCategoriesIntent = new Intent(getApplicationContext(), CategoriesActivity.class);
        startActivity(toCategoriesIntent);
    }

    @Override
    public void onNothingSelected() {

    }
}