package com.example.sander.bunqer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sander.bunqer.Sorter.CsvImportHelper;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class NewAccountActivity extends AppCompatActivity {

    TextView textView;
    ArrayList<Transaction> transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        getSupportActionBar().setTitle("Add new account");

        // hide unnecessary views
        findViewById(R.id.accountname).setVisibility(View.GONE);
        findViewById(R.id.accountnumber).setVisibility(View.GONE);
        findViewById(R.id.pathtocsv).setVisibility(View.GONE);
        findViewById(R.id.createAccountButton).setVisibility(View.GONE);

        // testing grounds


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
            transactions = CsvImportHelper.getTransactionList(getApplicationContext(), getIntent());

            textView.setText(transactions.toString());
        }
    }

    public void sendToMonth(View view) {
        Intent toMonthSelectorIntent = new Intent(this, MonthSelectorActivity.class);
        startActivity(toMonthSelectorIntent);
        finish();
    }
}
