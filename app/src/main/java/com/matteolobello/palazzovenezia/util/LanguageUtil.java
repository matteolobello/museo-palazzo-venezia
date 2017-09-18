package com.matteolobello.palazzovenezia.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.StringRes;
import android.util.DisplayMetrics;

import com.matteolobello.palazzovenezia.data.preference.PreferenceHandler;

import java.util.Locale;

public class LanguageUtil {

    public static final String[] LANGUAGES = {
            "en",
            "it",
            "es"
    };

    public static int getSpinnerIndex(Context context) {
        String lang = getLanguage(context);
        for (int i = 0; i < LANGUAGES.length; i++) {
            if (LANGUAGES[i].equals(lang)) {
                return i;
            }
        }

        return 0;
    }

    public static int getSpinnerIndex(String lang) {
        for (int i = 0; i < LANGUAGES.length; i++) {
            if (LANGUAGES[i].equals(lang)) {
                return i;
            }
        }

        return 0;
    }

    public static String getLanguage(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }

    public static void updateAppLanguage(Activity activity, String newLanguage, Class activityClass) {
        Locale newLocale = new Locale(newLanguage);
        Resources resources = activity.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = newLocale;
        resources.updateConfiguration(configuration, displayMetrics);

        PreferenceHandler.get().setValue(activity, PreferenceHandler.LANGUAGE_KEY, newLanguage);

        Intent refresh = new Intent(activity, activityClass);
        activity.startActivity(refresh);
        activity.finish();
        activity.overridePendingTransition(0, 0);
    }

    public static String getStringByLocale(Context context, @StringRes int stringRes, String language) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration = new Configuration(configuration);
        configuration.setLocale(new Locale(language));
        Context localizedContext = context.createConfigurationContext(configuration);
        return localizedContext.getResources().getString(stringRes);
    }
}
