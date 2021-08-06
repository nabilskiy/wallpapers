package wallgram.hd.wallpapers.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

import static android.os.Build.VERSION_CODES.N;

public class LocaleHelper {

    private final SharedPreferences prefs;

    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_RUSSIAN = "ru";
    private static final String LANGUAGE_KEY = "language_key";

    public LocaleHelper(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public Context setLocale(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(c, getLanguage());
        }

        return updateResourcesLegacy(c, getLanguage());
    }

    public void setNewLocale(Context c, String language) {
        persistLanguage(language);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(c, getLanguage());
            return;
        }

        updateResourcesLegacy(c, getLanguage());
    }

    private String getLanguage() {
        String locale = Locale.getDefault().getLanguage();
        return prefs.getString(LANGUAGE_KEY, locale);
    }

    @SuppressLint("ApplySharedPref")
    private void persistLanguage(String language) {
        prefs.edit().putString(LANGUAGE_KEY, language).commit();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration configuration = context.getResources().getConfiguration();
        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language, getLocale(context.getResources()).getCountry());
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context;
    }

    private static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return isAtLeastVersion(N) ? config.getLocales().get(0) : config.locale;
    }

    public static String getCountry(Resources res) {
        Configuration config = res.getConfiguration();
        return isAtLeastVersion(N) ? config.getLocales().get(0).getCountry() : config.locale.getCountry();
    }

    private static boolean isAtLeastVersion(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

}