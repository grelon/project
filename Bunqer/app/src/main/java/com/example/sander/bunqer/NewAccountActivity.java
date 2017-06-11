package com.example.sander.bunqer;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class NewAccountActivity extends AppCompatActivity {

    TextView textView;
    ArrayList<Transaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        getSupportActionBar().setTitle("Add new account");

        // get textview
        textView = (TextView) findViewById(R.id.tvTest);

        if (getIntent().getAction().equals("android.intent.action.SEND") &&
                getIntent().getType().equals("text/csv; charset=utf-8")) {
            Log.d("log", "SEND intent with CSV type detected");
            textView.setText("Nothing has been shared :(");

            // hide unnecessary views
            findViewById(R.id.accountname).setVisibility(View.GONE);
            findViewById(R.id.accountnumber).setVisibility(View.GONE);
            findViewById(R.id.pathtocsv).setVisibility(View.GONE);
            findViewById(R.id.createAccountButton).setVisibility(View.GONE);

            // get list of transaction objects
            transactions = CsvImportHelper(this).getTransactionList(getIntent());

            textView.setText(transactions.toString());
        }
    }

    public void sendToMonth(View view) {
        Intent toMonthSelectorIntent = new Intent(this, MonthSelectorActivity.class);
        startActivity(toMonthSelectorIntent);
        finish();
    }
}
