package com.example.sander.bunqer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class NewAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);
        getSupportActionBar().setTitle("Add new account");
    }

    public void sendToMonth(View view) {
        Intent toMonthSelectorIntent = new Intent(this, MonthSelectorActivity.class);
        startActivity(toMonthSelectorIntent);
        finish();
    }
}
