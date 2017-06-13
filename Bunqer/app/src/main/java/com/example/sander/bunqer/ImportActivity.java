package com.example.sander.bunqer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.Helpers.CsvImportHelper;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class ImportActivity extends AppCompatActivity {

    TextView textView;
    ArrayList<Transaction> transactions;
    DBManager dbManager;

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

        // get textview
        textView = (TextView) findViewById(R.id.tvTest);

        if (getIntent().getAction().equals("android.intent.action.SEND") &&
                getIntent().getType().equals("text/csv; charset=utf-8")) {
            Log.d("log", "SEND intent with CSV type detected");

            // hide unnecessary views
            findViewById(R.id.accountname).setVisibility(View.GONE);
            findViewById(R.id.accountnumber).setVisibility(View.GONE);
            findViewById(R.id.pathtocsv).setVisibility(View.GONE);
            findViewById(R.id.createAccountButton).setVisibility(View.GONE);

            // get list of transaction objects
            transactions = CsvImportHelper.getTransactionList(getApplicationContext(), getIntent());

            sendToMonth();

            // testing grounds
            dbManager = DBManager.getInstance(getApplicationContext());
            Log.d("log", "accounts: " + dbManager.readAccounts().toString());
            Log.d("log", "categories: " + dbManager.readCategories().toString());
            Log.d("log", "transactions: " + dbManager.readTransactions().toString());
        }
    }

    public void sendToMonth() {
        Intent toMonthIntent = new Intent(this, MonthActivity.class);
        startActivity(toMonthIntent);
        finish();
    }
}
