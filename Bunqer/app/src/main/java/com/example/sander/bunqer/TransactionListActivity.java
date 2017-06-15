package com.example.sander.bunqer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class TransactionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        ArrayList<Transaction> transactions;

        ListView lvTransactionList = (ListView) findViewById(R.id.transactionList);

        // got here with specific category in mind
        if (getIntent().getExtras().getSerializable("category") != null) {
            Category category = (Category) getIntent().getExtras().getSerializable("category");
            transactions = category.getTransactions();
        }

        // got here without specific category in mind
        else {
            transactions = DBManager.getInstance(this).readTransactions();
        }

        TransactionListAdapter transactionListAdapter

        ArrayAdapter<String> transactionArrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_list_item_1, transactions);

        lvTransactionList.setAdapter(transactionArrayAdapter);
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
