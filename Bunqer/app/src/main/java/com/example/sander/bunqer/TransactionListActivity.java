package com.example.sander.bunqer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class TransactionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        ListView lvTransactionList = (ListView) findViewById(R.id.transactionList);

        Transaction transaction1 = new Transaction();
        transaction1.setCounterparty("Albert Heijn");
        transaction1.setAmount("€ 14,94");
        transaction1.setDate("14-03-2017");

        Transaction transaction2 = new Transaction();
        transaction2.setCounterparty("Albert Heijn");
        transaction2.setAmount("€ 9,94");
        transaction2.setDate("17-03-2017");

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        ArrayList<String> transactionlist = new ArrayList<>();
        transactionlist.add("transaction1");
        transactionlist.add("transaction2");
        transactionlist.add("transaction3");


        ArrayAdapter<String> transactionArrayAdapter = new ArrayAdapter<String>(
                getApplicationContext(), android.R.layout.simple_list_item_1, transactionlist);

        lvTransactionList.setAdapter(transactionArrayAdapter);
    }
}
