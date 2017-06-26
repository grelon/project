package com.example.sander.bunqer.Helpers;
/*
 * Created by sander on 26-6-17.
 */

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class CurrencyFormatter implements IValueFormatter {

    private DecimalFormat decimalFormat;

    public CurrencyFormatter() {
        decimalFormat = new DecimalFormat("###,###,##0.00");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return "€ " + decimalFormat.format(value/100);
    }
}
