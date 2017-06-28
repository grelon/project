package com.example.sander.bunqer;
/*
 * Created by sander on 15-6-17.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

/**
 * Controls the action on the transaction list screen.
 */

public class TransactionListActivity extends AppCompatActivity {

    LinearLayoutManager mLayoutManager;
    RecyclerView mRvTransactionRecycler;
    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        ArrayList<Transaction> transactions;
        mRvTransactionRecycler = (RecyclerView) findViewById(R.id.transactionlist_recycler);

        // got here with specific category in mind
        if (getIntent().getExtras().getSerializable("category") != null) {
            category = (Category) getIntent().getExtras().getSerializable("category");
            transactions = category.getTransactions();
            getSupportActionBar().setTitle(category.getName());
        }
        // got here without specific category in mind
        else {
            transactions = DBManager.getInstance().readTransactions(null);
        }

        // show transaction list if there are transactions
        if (transactions.size() > 0) {
            mLayoutManager = new LinearLayoutManager(this);
            mRvTransactionRecycler.setLayoutManager(mLayoutManager);
            TransactionRecyclerAdapter mAdapter = new TransactionRecyclerAdapter(transactions, this);
            mRvTransactionRecycler.setAdapter(mAdapter);
        }
        // otherwise inform user there are no transactions
        else {
            mRvTransactionRecycler.setVisibility(View.INVISIBLE);
            TextView mTvEmptyListNotification = (TextView) findViewById(R.id.transactionlist_empty_list);
            mTvEmptyListNotification.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // send user back to chart with current category as extra
        Intent toChartActivity = new Intent(this, ChartActivity.class);
        toChartActivity.putExtra("category", category);
        startActivity(toChartActivity);
        finish();
    }
}
