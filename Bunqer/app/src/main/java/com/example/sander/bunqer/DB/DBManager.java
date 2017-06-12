package com.example.sander.bunqer.DB;
/*
 * Created by sander on 12-6-17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sander.bunqer.ModelClasses.Account;
import com.example.sander.bunqer.ModelClasses.Category;

import java.util.ArrayList;

public class DBManager {
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    // constructor
    public DBManager(Context context) {
        dbHelper = DBHelper.getInstance(context);
        db = dbHelper.getWritableDatabase();
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
}
