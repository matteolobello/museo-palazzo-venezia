package com.matteolobello.palazzovenezia.data.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.StringDef;

public class PreferenceHandler {

    public static final String LAUNCH_INTRODUCTION_KEY = "launch_introduction";

    @StringDef({LAUNCH_INTRODUCTION_KEY})
    public @interface PreferenceKey {
    }

    private static PreferenceHandler sInstance;

    private PreferenceHandler() {
    }

    public static PreferenceHandler get() {
        if (sInstance == null) {
            sInstance = new PreferenceHandler();
        }

        return sInstance;
    }


    public String getValue(Context context, @PreferenceKey String key) {
        return getSharedPreferences(context).getString(key, null);
    }

    public void setValue(Context context, @PreferenceKey String key, String value) {
        getSharedPreferences(context).edit().putString(key, value).apply();
    }

    private SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
