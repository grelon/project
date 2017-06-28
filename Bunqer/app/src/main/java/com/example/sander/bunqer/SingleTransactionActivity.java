package com.example.sander.bunqer;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.sander.bunqer.DB.DBManager;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class SingleTransactionActivity extends AppCompatActivity {

    private Transaction transaction;
    private TextView tvSingleTransactionCategory;
    private int originalCategoryId;

    View bottomSheetView;
    BottomSheetDialog bottomSheetDialog;
    BottomSheetBehavior bottomSheetBehavior;

    ArrayList<Category> stubCategories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_transaction);

        // get transaction object from intent
        transaction = (Transaction) getIntent().getExtras().getSerializable("transaction");

        // remember its category
        originalCategoryId = transaction.getCategoryId();

        // find views and set their contents
        setViews();

        // bottom sheet dialog setup
        bottomSheetView = getLayoutInflater().inflate(R.layout.bottomsheetdialog_change_category, null);
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                showCategories();
            }
        });
    }

    private void setViews() {
        // get views
        TextView tvSingleTransactionCounterpartyName = (TextView) findViewById(R.id.single_transaction_counterpartyName);
        TextView tvSingleTransactionCounterpartyAccount = (TextView) findViewById(R.id.single_transaction_counterpartyAccount);
        TextView tvSingleTransactionDate = (TextView) findViewById(R.id.single_transaction_date);
        TextView tvSingleTransactionAmount = (TextView) findViewById(R.id.single_transaction_amount);
        TextView tvSingleTransactionDescription = (TextView) findViewById(R.id.single_transaction_description);
        tvSingleTransactionCategory = (TextView) findViewById(R.id.single_transaction_category);
        ImageButton ibSingleTransactionChangeCategory = (ImageButton) findViewById(R.id.single_transaction_change_category);

        // set views
        tvSingleTransactionCounterpartyName.setText(transaction.getCounterpartyName());
        tvSingleTransactionCounterpartyAccount.setText(transaction.getCounterpartyAccount());
        tvSingleTransactionDate.setText(transaction.getDate());
        tvSingleTransactionAmount.setText(transaction.getFormattedAmount());
        tvSingleTransactionDescription.setText(transaction.getDescription());
        tvSingleTransactionCategory.setText(transaction.getCategory());

        // set onClickListener for the change category button
        ibSingleTransactionChangeCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                bottomSheetDialog.show();
            }
        });
    }

    private void showCategories() {
        // get the stubs of the categories
        if(stubCategories.isEmpty()) {
            ArrayList<Category> categories = DBManager.getInstance().readCategories(null);
            getStubs(categories);
        }

        // create list of names of stubcategories for the adapter
        ArrayList<String> stringStubCategories = new ArrayList<>();
        for (Category category:stubCategories) {
            stringStubCategories.add(category.getName());
        }

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, stringStubCategories);

        ListView listView = (ListView) bottomSheetDialog.findViewById(R.id.single_transaction_dialog_categories);

        listView.setAdapter(categoriesAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category selectedCategory = stubCategories.get(position);
                transaction.setCategoryId(selectedCategory.getId());
                transaction.setCategory(selectedCategory.getName());
                DBManager.getInstance().updateTransaction(transaction);
                bottomSheetDialog.hide();
                tvSingleTransactionCategory.setText(transaction.getCategory());
            }
        });
    }

    // go through categories recursively and only gather the stubs
    private void getStubs(ArrayList<Category> categories) {
        for (Category category:categories) {
            if (category.getSubcategories().size() > 0) {
                categories = category.getSubcategories();
                getStubs(categories);
            }
            else {
                stubCategories.add(category);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent toTransactionListIntent = new Intent(this, TransactionListActivity.class);

        // give TransactionList a fresh copy of the category
        Category category = DBManager.getInstance().readCategories(originalCategoryId).get(0);
        toTransactionListIntent.putExtra("category", category);

        startActivity(toTransactionListIntent);
        finish();
    }

    @Override
    protected void onDestroy() {
        bottomSheetDialog.dismiss();
        super.onDestroy();
    }
}