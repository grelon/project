package com.example.sander.bunqer.DB;
/*
 * Created by sander on 12-6-17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Defines how the database is to be created and how one could get an instance of it.
 */

class DBHelper extends SQLiteOpenHelper {
    private static DBHelper dbHelper;
    private static SQLiteDatabase db;

    private static final String DATABASE_NAME = "MyApp.db";
    private static final int DATABASE_VERSION = 1;

    /* Categories table */
    // table name
    static final String TABLE_CATEGORIES = "categories";

    // column names
    static final String CATEGORY_ID = "category_id";
    static final String CATEGORY_ACCOUNT_ID = "account_id";
    static final String CATEGORY_PARENT_ID = "parent_id";
    static final String CATEGORY_NAME = "name";

    // table creation query
    private static final String CREATE_CATEGORIES_TABLE =
            "CREATE TABLE " + TABLE_CATEGORIES + " ( " +
                    CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    CATEGORY_ACCOUNT_ID + " INTEGER NOT NULL, " +
                    CATEGORY_PARENT_ID + " INTEGER NOT NULL, " +
                    CATEGORY_NAME + " TEXT NOT NULL);";

    /* Transactions table */
    // table name
    static final String TABLE_TRANSACTIONS = "transactions";

    // column names
    static final String TRANSACTION_ID = "transaction_id";
    static final String TRANSACTION_CATEGORY_ID = "category_id";
    static final String TRANSACTION_ACCOUNT_ID = "account_id";
    static final String TRANSACTION_DATE = "date";
    static final String TRANSACTION_AMOUNT = "amount";
    static final String TRANSACTION_COUNTERPARTY_ACCOUNT = "counterparty_account";
    static final String TRANSACTION_COUNTERPARTY_NAME = "counterparty_name";
    static final String TRANSACTION_DESCRIPTION = "description";

    // table creation query
    private static final String CREATE_TRANSACTIONS_TABLE =
            "CREATE TABLE " + TABLE_TRANSACTIONS + " ( " +
                    TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TRANSACTION_CATEGORY_ID + " INTEGER NOT NULL, " +
                    TRANSACTION_ACCOUNT_ID + " INTEGER NOT NULL, " +
                    TRANSACTION_DATE + " TEXT NOT NULL, " +
                    TRANSACTION_AMOUNT + " TEXT NOT NULL, " +
                    TRANSACTION_COUNTERPARTY_ACCOUNT + " TEXT, " +
                    TRANSACTION_COUNTERPARTY_NAME + " TEXT, " +
                    TRANSACTION_DESCRIPTION + " TEXT NOT NULL);";


    /* Accounts table */
    // table name
    static final String TABLE_ACCOUNTS = "accounts";

    // column names
    static final String ACCOUNT_ID = "account_id";
    static final String ACCOUNT_NUMBER = "account_number";
    static final String ACCOUNT_NAME = "account_name";

    // table creation query
    private static final String CREATE_ACCOUNTS_TABLE =
            "CREATE TABLE " + TABLE_ACCOUNTS + " ( " +
                    ACCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ACCOUNT_NUMBER + " TEXT NOT NULL, " +
                    ACCOUNT_NAME + " TEXT NOT NULL);";


    /* Create, upgrade and get database */
    // DON'T use this constructor to instantiate dbHelper!
    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // use getInstance() to instantiate dbHelper
    static synchronized DBHelper getInstance() {
        if (dbHelper == null) {
            dbHelper = new DBHelper(MyApp.getContext());
        }
        db = dbHelper.getWritableDatabase();
        return dbHelper;
    }

    // create database
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_TRANSACTIONS_TABLE);
        db.execSQL(CREATE_ACCOUNTS_TABLE);
    }

    // upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: 12-6-17 Do I need to do something here or not?
    }


}
