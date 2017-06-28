package com.example.sander.bunqer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        setContentView(R.layout.activity_import);
        getSupportActionBar().setTitle("Importing transactions");

        // hide unnecessary views
        findViewById(R.id.accountname).setVisibility(View.GONE);
        findViewById(R.id.accountnumber).setVisibility(View.GONE);
        findViewById(R.id.pathtocsv).setVisibility(View.GONE);
        findViewById(R.id.createAccountButton).setVisibility(View.GONE);

        // get textview
        textView = (TextView) findViewById(R.id.tvTest);


    }

    public void sendToMonth() {
        Intent toMonthIntent = new Intent(this, ChartActivity.class);
        startActivity(toMonthIntent);
        finish();
    }
}
