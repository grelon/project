package com.example.sander.bunqer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class TransactionListActivity extends AppCompatActivity {

    LinearLayoutManager mLayoutManager;
    RecyclerView mRvTransactionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        mRvTransactionList = (RecyclerView) findViewById(R.id.transactionList);
        mLayoutManager = new LinearLayoutManager(this);
        mRvTransactionList.setLayoutManager(mLayoutManager);



        ArrayList<Transaction> transactions;

        // got here with specific category in mind
        if (getIntent().getExtras().getSerializable("category") != null) {
            Category category = (Category) getIntent().getExtras().getSerializable("category");
            transactions = category.getTransactions();
        }

        // got here without specific category in mind
        else {
            transactions = DBManager.getInstance(this).readTransactions();
        }




        lvTransactionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent toTransactionIntent = new Intent(getApplicationContext(),
                        SingleTransactionActivity.class);
                startActivity(toTransactionIntent);
            }
        });
    }
}
