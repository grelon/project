package com.example.sander.bunqer;
/*
 * Created by sander on 18-6-17.
 */

import android.app.Application;
import android.content.Context;

/**
 * This class is used for easy reference to the application context (e.g. for the DBHelper)
 *
 * credit goes to: https://stackoverflow.com/q/987072
 */

public class Bunqer extends Application{
    private static Bunqer instance;

    public Bunqer() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
}
