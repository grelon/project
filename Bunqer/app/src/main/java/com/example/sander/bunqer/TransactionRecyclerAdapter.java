package com.example.sander.bunqer;
/*
 * Created by sander on 15-6-17.
 */

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter<TransactionRecyclerAdapter.TransactionHolder> {

    private ArrayList<Transaction> mTransactions;

    public static class TransactionHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView tvTransactionDescription;
        private TextView tvTransactionDate;
        private TextView tvTransactionAmount;
        private TextView tvTransactionCategory;

        public TransactionHolder(View itemView) {
            super(itemView);

            tvTransactionDescription = (TextView)itemView.findViewById(R.id.transaction_recycler_tv_description);
            tvTransactionDate = (TextView)itemView.findViewById(R.id.transaction_recycler_tv_date);
            tvTransactionAmount = (TextView)itemView.findViewById(R.id.transaction_recycler_tv_amount);
            tvTransactionCategory = (TextView)itemView.findViewById(R.id.transaction_recycler_tv_category);
            itemView.setOnClickListener(this);
        }

        public void sayHello() {
            Log.d("log", "hello!");
        }

        @Override
        public void onClick(View v) {
            Log.d("log", "transaction clicked");

//            Context context = itemView.getContext();
//            Intent showPhotoIntent = new Intent(context, PhotoActivity.class);
//            showPhotoIntent.putExtra(PHOTO_KEY, mPhoto);
//            context.startActivity(showPhotoIntent);
        }

        public void bindTransaction(Transaction transaction) {
            tvTransactionDescription.setText(transaction.getDescription());
            tvTransactionDate.setText(transaction.getDate());
            tvTransactionAmount.setText(String.valueOf(transaction.getAmount()));
            tvTransactionCategory.setText(transaction.getCategory());
        }
    }


    public TransactionRecyclerAdapter(ArrayList<Transaction> mTransactions) {
        this.mTransactions = mTransactions;
    }

    @Override
    public TransactionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_row, parent, false);
        return new TransactionHolder(inflatedView);
    }

    public void onBindViewHolder(TransactionRecyclerAdapter.TransactionHolder holder, int position) {
        Transaction transaction = mTransactions.get(position);
        holder.bindTransaction(transaction);
    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }
}
