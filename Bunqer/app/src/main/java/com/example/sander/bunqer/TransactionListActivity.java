package com.example.sander.bunqer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class TransactionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        ListView lvTransactionList = (ListView) findViewById(R.id.transactionList);

//////        Transaction transaction1 = new Transaction();
//////        transaction1.setCounterparty_name("Albert Heijn");
//////        transaction1.setAmount("€ 14,94");
//////        transaction1.setDate("14-03-2017");
//////
//////        Transaction transaction2 = new Transaction();
//////        transaction2.setCounterparty_name("Albert Heijn");
//////        transaction2.setAmount("€ 9,94");
//////        transaction2.setDate("17-03-2017");
//////
//////        ArrayList<Transaction> transactions = new ArrayList<>();
//////        transactions.add(transaction1);
//////        transactions.add(transaction2);
//////
//////        ArrayList<String> transactionlist = new ArrayList<>();
//////        transactionlist.add("transaction1");
//////        transactionlist.add("transaction2");
//////        transactionlist.add("transaction3");
////
////
////        ArrayAdapter<String> transactionArrayAdapter = new ArrayAdapter<String>(
////                getApplicationContext(), android.R.layout.simple_list_item_1, transactionlist);
//
//        lvTransactionList.setAdapter(transactionArrayAdapter);
//        lvTransactionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent toTransactionIntent = new Intent(getApplicationContext(),
//                        SingleTransactionActivity.class);
//                startActivity(toTransactionIntent);
//            }
//        });
    }
}
