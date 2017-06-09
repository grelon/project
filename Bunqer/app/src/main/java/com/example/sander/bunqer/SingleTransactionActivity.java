package com.example.sander.bunqer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SingleTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_transaction);

        getSupportActionBar().setTitle("Albert Heijn, 06-06-2017 21:15");
    }
}
