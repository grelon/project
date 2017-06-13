package com.example.sander.bunqer.DB;
/*
 * Created by sander on 12-6-17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sander.bunqer.ModelClasses.Account;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class DBManager {
    private static DBHelper dbHelper;
    private static SQLiteDatabase db;
    private static DBManager dbManager;

    // constructor
    private DBManager(Context context) {
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
    }

    public static synchronized DBManager getInstance(Context context) {
        if (dbManager == null) {
            dbManager = new DBManager(context);
        }
        return dbManager;
    }

    private static void setupDatabase() {

    }

    // CRUD: accounts table
    public void createAccount(Account account) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.ACCOUNT_NUMBER, account.getNumber());
        values.put(DBHelper.ACCOUNT_NAME, account.getName());
        db.insert(DBHelper.TABLE_ACCOUNTS, null, values);
    }

    public ArrayList<Account> readAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();

        // columns to read
        String[] columns = new String[] {
                DBHelper.ACCOUNT_ID,
                DBHelper.ACCOUNT_NUMBER,
                DBHelper.ACCOUNT_NAME };

        // create cursor object to read previously defined columns
        Cursor cursor = db.query(DBHelper.TABLE_ACCOUNTS, columns, null, null, null, null, null);

        // move over rows with cursor
        if (cursor.moveToFirst()) {
            do {
                // get needed data from current row
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNT_ID));
                String number = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNT_NUMBER));
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNT_NAME));

                // create category object with data
                Account account = new Account(id, number, name);
                accounts.add(account);
            }
            // until end of cursor object has been reached
            while (cursor.moveToNext());
        }

        // wrap up and return
        cursor.close();
        return accounts;
    }

    public void updateAccount(Account account) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.ACCOUNT_NAME, account.getName());
        db.update(DBHelper.TABLE_ACCOUNTS, values, DBHelper.ACCOUNT_ID + " = ?",
                new String[] {String.valueOf(account.getId())});
    }

    public void deleteAccount(Account account) {
        db.delete(DBHelper.TABLE_ACCOUNTS, DBHelper.ACCOUNT_ID + " = ?",
                new String[] {String.valueOf(account.getId())});
    }

    // CRUD: categories table
    public void createCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.CATEGORY_ACCOUNT_ID, category.getAccount_id());
        values.put(DBHelper.CATEGORY_NAME, category.getName());
        db.insert(DBHelper.TABLE_CATEGORIES, null, values);
    }

    public ArrayList<Category> readCategories() {
        ArrayList<Category> categories = new ArrayList<>();

        // columns to read
        String[] columns = new String[] {
                DBHelper.CATEGORY_ID,
                DBHelper.CATEGORY_ACCOUNT_ID,
                DBHelper.CATEGORY_NAME };

        // create cursor object to read previously defined columns
        Cursor cursor = db.query(DBHelper.TABLE_CATEGORIES, columns, null, null, null, null, null);

        // move over rows with cursor
        if (cursor.moveToFirst()) {
            do {
                // get needed data from current row
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.CATEGORY_ID));
                int account_id = cursor.getInt(cursor.getColumnIndex(DBHelper.CATEGORY_ACCOUNT_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY_NAME));

                // create category object with data
                Category category = new Category(id, account_id, name);
                categories.add(category);
            }
            // until end of cursor object has been reached
            while (cursor.moveToNext());
        }

        // wrap up and return
        cursor.close();
        return categories;
    }

    public void updateCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.CATEGORY_NAME, category.getName());
        db.update(DBHelper.TABLE_CATEGORIES, values, DBHelper.CATEGORY_ID + " = ?",
                new String[] {String.valueOf(category.getId())});
    }

    public void deleteCategory(Category category) {
        db.delete(DBHelper.TABLE_CATEGORIES, DBHelper.CATEGORY_ID + " = ?",
                new String[] {String.valueOf(category.getId())});
    }

    // CRUD: transactions table
    public void createTransaction(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.TRANSACTION_CATEGORY_ID, transaction.getCategory_id());
        values.put(DBHelper.TRANSACTION_ACCOUNT_ID, transaction.getAccount_id());
        values.put(DBHelper.TRANSACTION_DATE, transaction.getDate());
        values.put(DBHelper.TRANSACTION_AMOUNT, transaction.getAmount());
        values.put(DBHelper.TRANSACTION_COUNTERPARTY_ACCOUNT, transaction.getCounterparty_account());
        values.put(DBHelper.TRANSACTION_COUNTERPARTY_NAME, transaction.getCounterparty_name());
        values.put(DBHelper.TRANSACTION_DESCRIPTION, transaction.getDescription());
        db.insert(DBHelper.TABLE_TRANSACTIONS, null, values);
    }

    public ArrayList<Transaction> readTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        // columns to read
        String[] columns = new String[] {
            DBHelper.TRANSACTION_ID,
            DBHelper.TRANSACTION_CATEGORY_ID,
            DBHelper.TRANSACTION_ACCOUNT_ID,
            DBHelper.TRANSACTION_DATE,
            DBHelper.TRANSACTION_AMOUNT,
            DBHelper.TRANSACTION_COUNTERPARTY_ACCOUNT,
            DBHelper.TRANSACTION_COUNTERPARTY_NAME,
            DBHelper.TRANSACTION_DESCRIPTION };

        // create cursor object to read previously defined columns
        Cursor cursor = db.query(DBHelper.TABLE_TRANSACTIONS, columns, null, null, null, null, null);

        // move over rows with cursor
        if (cursor.moveToFirst()) {
            do {
                // get needed data from current row
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.TRANSACTION_ID));
                int category_id = cursor.getInt(cursor.getColumnIndex(DBHelper.TRANSACTION_CATEGORY_ID));
                Log.d("log", "category_id: " + category_id);
                Log.d("log", "categories: " + readCategories().toString());
                String category_name = readCategories().get(category_id-1).getName();
                int account_id = cursor.getInt(cursor.getColumnIndex(DBHelper.TRANSACTION_ACCOUNT_ID));
                String account_name = readAccounts().get(account_id-1).getName();
                String date = cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_DATE));
                String amount = cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_AMOUNT));
                String counterparty_account = cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_COUNTERPARTY_ACCOUNT));
                String counterparty_name = cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_COUNTERPARTY_NAME));
                String description = cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_DESCRIPTION));

                // create category object with data
                Transaction transaction = new Transaction(
                        id,
                        date,
                        amount,
                        account_id,
                        account_name,
                        category_name,
                        category_id,
                        counterparty_name,
                        counterparty_account,
                        description);
                transactions.add(transaction);
            }
            // until end of cursor object has been reached
            while (cursor.moveToNext());
        }

        // wrap up and return
        cursor.close();
        return transactions;
    }

    public void updateTransaction(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.TRANSACTION_CATEGORY_ID, transaction.getCategory_id());
        db.update(DBHelper.TABLE_TRANSACTIONS, values, DBHelper.TRANSACTION_ID + " = ?",
                new String[] {String.valueOf(transaction.getCategory_id())});
    }
}
