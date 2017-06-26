package com.example.sander.bunqer.DB;
/*
 * Created by sander on 18-6-17.
 */

import android.app.Application;
import android.content.Context;

/**
 * By extending the Application class, this class provides easy reference to the application context
 * (e.g. for the DBHelper).
 *
 * Stolen from: https://stackoverflow.com/q/987072
 */

public class MyApp extends Application{
    private static MyApp instance;

    public MyApp() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}
