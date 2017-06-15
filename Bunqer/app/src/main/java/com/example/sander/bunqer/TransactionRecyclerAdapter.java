package com.example.sander.bunqer;
/*
 * Created by sander on 15-6-17.
 */

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class TransactionRecyclerAdapter extends RecyclerView.Adapter {

    private ArrayList<Transaction> mTransactions;

    public TransactionRecyclerAdapter(ArrayList<Transaction> mTransactions) {
        this.mTransactions = mTransactions;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
