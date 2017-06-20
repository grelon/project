package com.example.sander.bunqer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.sander.bunqer.ModelClasses.Transaction;

public class SingleTransactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_transaction);

        // get transaction object from intent
        Transaction transaction = (Transaction) getIntent().getExtras().getSerializable("transaction");

        // get views
        TextView tvSingleTransactionCounterpartyName = (TextView) findViewById(R.id.single_transaction_counterpartyName);
        TextView tvSingleTransactionCounterpartyAccount = (TextView) findViewById(R.id.single_transaction_counterpartyAccount);
        TextView tvSingleTransactionDate = (TextView) findViewById(R.id.single_transaction_date);
        TextView tvSingleTransactionAmount = (TextView) findViewById(R.id.single_transaction_amount);
        TextView tvSingleTransactionDescription = (TextView) findViewById(R.id.single_transaction_description);

        // set views
        tvSingleTransactionCounterpartyName.setText(transaction.getCounterpartyName());
        tvSingleTransactionCounterpartyAccount.setText(transaction.getCounterpartyName());
        tvSingleTransactionDate.setText(transaction.getDate());
        tvSingleTransactionAmount.setText(transaction.getFormattedAmount());
        tvSingleTransactionDescription.setText(transaction.getDescription());
    }
}
