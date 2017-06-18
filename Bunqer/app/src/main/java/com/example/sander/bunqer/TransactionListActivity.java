package com.example.sander.bunqer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class TransactionListActivity extends AppCompatActivity {

    LinearLayoutManager mLayoutManager;
    RecyclerView mRvTransactionReycler;
    private TransactionRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        mRvTransactionReycler = (RecyclerView) findViewById(R.id.transactionList);
        mLayoutManager = new LinearLayoutManager(this);
        mRvTransactionReycler.setLayoutManager(mLayoutManager);

        ArrayList<Transaction> transactions;

        // got here with specific category in mind
        if (getIntent().getExtras().getSerializable("category") != null) {
            Category category = (Category) getIntent().getExtras().getSerializable("category");
            transactions = category.getTransactions();
        }

        // got here without specific category in mind
        else {
            transactions = DBManager.getInstance().readTransactions(null);
        }

        mAdapter = new TransactionRecyclerAdapter(transactions);
        mRvTransactionReycler.setAdapter(mAdapter);
    }


}
