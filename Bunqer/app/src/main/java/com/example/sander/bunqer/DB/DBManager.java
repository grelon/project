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

    private static final int ROOT = -1;

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
                // create account from data
                Account account = new Account();
                account.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.ACCOUNT_ID)));
                account.setNumber(cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNT_NUMBER)));
                account.setName(cursor.getString(cursor.getColumnIndex(DBHelper.ACCOUNT_NAME)));

                // add account to account list
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
        ArrayList<Category> allCategories = new ArrayList<>();

        // columns to read
        String[] columns = new String[] {
                DBHelper.CATEGORY_ID,
                DBHelper.CATEGORY_ACCOUNT_ID,
                DBHelper.CATEGORY_PARENT_ID,
                DBHelper.CATEGORY_NAME };

        Cursor cursor = db.query(DBHelper.TABLE_CATEGORIES, columns, null, null, null, null, null);

        // move over rows with cursor
        if (cursor.moveToFirst()) {
            do {
                // create category object from data
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.CATEGORY_ID)));
                category.setAccountId(cursor.getInt(cursor.getColumnIndex(DBHelper.CATEGORY_ACCOUNT_ID)));
                category.setParentId(cursor.getInt(cursor.getColumnIndex(DBHelper.CATEGORY_PARENT_ID)));
                category.setName(cursor.getString(cursor.getColumnIndex(DBHelper.CATEGORY_NAME)));

                // add category to list of categories
                allCategories.add(category);
            }
            // until end of cursor object has been reached
            while (cursor.moveToNext());
        }
        cursor.close();

        // populate subcategory arrays of all categories
        for (Category category:allCategories) {
            // populate category with transactions if any exist
            category.updateTransactions();

            // add categories to their parent categories, if any
            for (Category parentCategory: allCategories) {
                if (parentCategory.getId() == category.getParentId()) {
                    parentCategory.addSubcategory(category);
                }
            }
        }

        // if a categoryId isn't specified, return a list of rootcategories
        if (categoryId == null) {
            ArrayList<Category> rootCategories = new ArrayList<>();
            for (Category category:allCategories){
                if (category.getParentId() == ROOT) {
                    rootCategories.add(category);
                }
            }
            return rootCategories;
        }

        // otherwise, return a list with only the specified category
        else {
            ArrayList<Category> singleCategory = new ArrayList<>();
            for (Category category:allCategories) {
                if (category.getId() == categoryId) {
                    singleCategory.add(category);
                }
            }
            return singleCategory;
        }
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
     * with the specified categoryId will be returned.
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

        // create cursor object to read all previously defined columns...
        Cursor cursor;
        if (categoryId == null) {
            // ... that exist
            cursor = db.query(DBHelper.TABLE_TRANSACTIONS, columns, null, null, null, null, null);
        }
        else {
            // ... only where categoryId is categoryID
            cursor = db.query(DBHelper.TABLE_TRANSACTIONS, columns,
                    DBHelper.TRANSACTION_CATEGORY_ID + " = ?", new String[]{categoryId.toString()},
                    null, null, null);
        }

        // move over rows with cursor, until end of cursor object has been reached
        if (cursor.moveToFirst()) {
            do {
                // create transaction object from data
                Transaction transaction = new Transaction();
                transaction.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.TRANSACTION_ID)));
                transaction.setCategoryId(cursor.getInt(cursor.getColumnIndex(DBHelper.TRANSACTION_CATEGORY_ID)));
                transaction.setAccountId(cursor.getInt(cursor.getColumnIndex(DBHelper.TRANSACTION_ACCOUNT_ID)));
                transaction.setAccount(readAccounts().get(transaction.getAccountId()-1).getName());
                transaction.setDate(cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_DATE)));
                transaction.setAmount(cursor.getInt(cursor.getColumnIndex(DBHelper.TRANSACTION_AMOUNT)));
                transaction.setCounterpartyAccount(cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_COUNTERPARTY_ACCOUNT)));
                transaction.setCounterpartyName(cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_COUNTERPARTY_NAME)));
                transaction.setDescription(cursor.getString(cursor.getColumnIndex(DBHelper.TRANSACTION_DESCRIPTION)));

                // add transaction to transactionlist
                transactions.add(transaction);
            }
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
                new String[] {String.valueOf(transaction.getId())});
    }
}
