package com.husseinsilver.store.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static SharedPreferencesManager instance;
    private final SharedPreferences prefs;

    private SharedPreferencesManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }

    public void saveUserId(String userId) {
        prefs.edit().putString(Constants.PREF_USER_ID, userId).apply();
    }

    public String getUserId() {
        return prefs.getString(Constants.PREF_USER_ID, null);
    }

    public void saveUserName(String name) {
        prefs.edit().putString(Constants.PREF_USER_NAME, name).apply();
    }

    public String getUserName() {
        return prefs.getString(Constants.PREF_USER_NAME, null);
    }

    public void saveUserEmail(String email) {
        prefs.edit().putString(Constants.PREF_USER_EMAIL, email).apply();
    }

    public String getUserEmail() {
        return prefs.getString(Constants.PREF_USER_EMAIL, null);
    }

    /** Cache last good silver ounce price in ILS. */
    public void saveSilverPriceILS(double price) {
        prefs.edit().putFloat(Constants.PREF_SILVER_PRICE_ILS, (float) price).apply();
    }

    /** Returns the last cached silver ounce price in ILS, or 0 if never set. */
    public double getSilverPriceILS() {
        return prefs.getFloat(Constants.PREF_SILVER_PRICE_ILS, 0f);
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }
}
