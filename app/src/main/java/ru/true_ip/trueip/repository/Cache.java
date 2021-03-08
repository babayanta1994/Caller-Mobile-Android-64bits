package ru.true_ip.trueip.repository;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by user on 13-Sep-17.
 */

public class Cache {

    public static final String TOKEN_KEY = "TOKEN_KEY";

    private SharedPreferences preferences;

    public Cache(Context context) {
        this.preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void saveString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public void saveInt(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void saveBoolean(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public int getInt(String key) {
        return preferences.getInt(key, -1);
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean isContains(String key) {
        return preferences.contains(key);
    }
}
