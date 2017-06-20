package com.example.sander.bunqer.DB;
/*
 * Created by sander on 12-6-17.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sander.bunqer.Helpers.CategoryHelper;
import com.example.sander.bunqer.ModelClasses.Account;
import com.example.sander.bunqer.ModelClasses.Category;
import com.example.sander.bunqer.ModelClasses.Transaction;

import java.util.ArrayList;

public class DBManager {
    private static DBHelper dbHelper;
    private static SQLiteDatabase db;
    private static DBManager dbManager;

    // constructor
    private DBManager() {
        dbHelper = DBHelper.getInstance();
        db = dbHelper.getWritableDatabase();
    }

    public static synchronized DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
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
        values.put(DBHelper.CATEGORY_ACCOUNT_ID, category.getAccountId());
        values.put(DBHelper.CATEGORY_NAME, category.getName());
        values.put(DBHelper.CATEGORY_PARENT_ID, category.getParentId());
        db.insert(DBHelper.TABLE_CATEGORIES, null, values);
    }

    public ArrayList<Category> readCategories(Integer categoryId) {
        ArrayList<Category> categories = new ArrayList<>();

        // columns to read
        String[] columns = new String[] {
                DBHelper.CATEGORY_ID,
                DBHelper.CATEGORY_ACCOUNT_ID,
                DBHelper.CATEGORY_PARENT_ID,
                DBHelper.CATEGORY_NAME };

        Cursor cursor;
        if (categoryId == null) {
            // create cursor object to read previously defined columns
            cursor = db.query(DBHelper.TABLE_CATEGORIES, columns, null, null, null, null, null);
        }
        else {
            // create cursor object to read previously defined columns only where categoryId is categoryID
            cursor = db.query(DBHelper.TABLE_CATEGORIES, columns,
                    DBHelper.CATEGORY_ID + " = ?", new String[]{categoryId.toString()},
                    null, null, null);
        }

        // move over rows with cursor
        if (cursor.moveToFirst()) {
            do {
                // get needed data from current row
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.CATEGORY_ID));
                int accountId = cursor.getInt(cursor.getColumnIndex(DBHelper.CATEGORY_ACCOUNT_ID));
                int parentId = cursor.getInt(cursor.getColumnIndex(DBHelper.CATEGORY_PARENT_ID));
                String name = cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY_NAME));

                // create category object with data
                Category category = new Category(id, parentId, accountId, name);

                // fill category with transactions
                category.setTransactions(readTransactions(category.getId()));

                categories.add(category);
            }
            // until end of cursor object has been reached
            while (cursor.moveToNext());
        }

        for (Category category:categories) {
            if (category.getParentId() != 0) {
                for (Category parentCategory: categories) {
                    if (parentCategory.getId() == category.getParentId()) {
                        parentCategory.addSubcategory(category);
                    }
                }
            }
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
        // categorize all child transactions as uncategorized
        for (Transaction transaction:category.getTransactions()) {
            transaction.setCategoryId(CategoryHelper.UNCATEGORIZED);
            updateTransaction(transaction);
        }

        // delete category
        db.delete(DBHelper.TABLE_CATEGORIES, DBHelper.CATEGORY_ID + " = ?",
                new String[] {String.valueOf(category.getId())});

    }

    // CRUD: transactions table
    public void createTransaction(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.TRANSACTION_CATEGORY_ID, transaction.getCategoryId());
        values.put(DBHelper.TRANSACTION_ACCOUNT_ID, transaction.getAccountId());
        values.put(DBHelper.TRANSACTION_DATE, transaction.getDate());
        values.put(DBHelper.TRANSACTION_AMOUNT, String.valueOf(transaction.getAmount()));
        values.put(DBHelper.TRANSACTION_COUNTERPARTY_ACCOUNT, transaction.getCounterpartyAccount());
        values.put(DBHelper.TRANSACTION_COUNTERPARTY_NAME, transaction.getCounterpartyName());
        values.put(DBHelper.TRANSACTION_DESCRIPTION, transaction.getDescription());
        db.insert(DBHelper.TABLE_TRANSACTIONS, null, values);
    }

    /**
     * Returns all transactions if 'null' is given as argument. If categoryId is not null, only rows
     * with that categoryId will be returned.
     *
     * @param categoryId
     * @return
     */
    public ArrayList<Transaction> readTransactions(Integer categoryId) {
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


        Cursor cursor;
        if (categoryId == null) {
            // create cursor object to read previously defined columns
            cursor = db.query(DBHelper.TABLE_TRANSACTIONS, columns, null, null, null, null, null);
        }
        else {
            // create cursor object to read previously defined columns only where categoryId is categoryID
            cursor = db.query(DBHelper.TABLE_TRANSACTIONS, columns,
                    DBHelper.TRANSACTION_CATEGORY_ID + " = ?", new String[]{categoryId.toString()},
                    null, null, null);
        }


        // move over rows with cursor
        if (cursor.moveToFirst()) {
            do {
                // get needed data from current row
                int id = cursor.getInt(cursor.getColumnIndex(DBHelper.TRANSACTION_ID));
                int catId = cursor.getInt(cursor.getColumnIndex(DBHelper.TRANSACTION_CATEGORY_ID));
                int accountId = cursor.getInt(cursor.getColumnIndex(DBHelper.TRANSACTION_ACCOUNT_ID));
                String accountName = readAccounts().get(accountId-1).getName();
                String date = cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_DATE));
                int amount = cursor.getInt(cursor.getColumnIndex(DBHelper.TRANSACTION_AMOUNT));
                String counterpartyAccount = cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_COUNTERPARTY_ACCOUNT));
                String counterpartyName = cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_COUNTERPARTY_NAME));
                String description = cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_DESCRIPTION));

                // create category object with data
                Transaction transaction = new Transaction(
                        id,
                        date,
                        amount,
                        accountId,
                        accountName,
                        catId,
                        counterpartyName,
                        counterpartyAccount,
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
        values.put(DBHelper.TRANSACTION_CATEGORY_ID, transaction.getCategoryId());
        db.update(DBHelper.TABLE_TRANSACTIONS, values, DBHelper.TRANSACTION_ID + " = ?",
                new String[] {String.valueOf(transaction.getCategoryId())});
    }
}
