package wallgram.hd.wallpapers.views.ratedialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Date;

public final class PreferenceHelper {

    private static final String PREF_FILE_NAME = "android_rate_pref_file";

    private static final String PREF_KEY_INSTALL_DATE = "android_rate_install_date";
    private static final String PREF_KEY_LAUNCH_TIMES = "android_rate_launch_times";
    private static final String PREF_KEY_IS_AGREE_SHOW_DIALOG = "android_rate_is_agree_show_dialog";
    private static final String PREF_KEY_REMIND_INTERVAL = "android_rate_remind_interval";
    private static final String PREF_KEY_FIRST_LAUNCH = "android_first_launch_app";

    private static final String PREF_KEY_ADS_STATE = "android_ads_show_state";
    private static final String PREF_KEY_IS_LIMITED_DOWNLOAD = "android_is_limited_download";

    private static final String PREF_KEY_INSTALL_INTERVAL = "android_rate_install_interval";
    private static final String PREF_KEY_INSTALL_TIMES = "android_rate_install_times";

    private static final String PREF_WALLPAPERS_ARRAY = "android_wallpapers_array";

    private PreferenceHelper() {
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    private static Editor getPreferencesEditor(Context context) {
        return getPreferences(context).edit();
    }

    /**
     * Clear data in shared preferences.<br/>
     *
     * @param context context
     */
    static void clearSharedPreferences(Context context) {
        Editor editor = getPreferencesEditor(context);
        editor.remove(PREF_KEY_INSTALL_DATE);
        editor.remove(PREF_KEY_LAUNCH_TIMES);
        editor.apply();
    }

    /**
     * Set agree flag about show dialog.<br/>
     * If it is false, rate dialog will never shown unless data is cleared.
     *
     * @param context context
     * @param isAgree agree with showing rate dialog
     */
    static void setAgreeShowDialog(Context context, boolean isAgree) {
        Editor editor = getPreferencesEditor(context);
        editor.putBoolean(PREF_KEY_IS_AGREE_SHOW_DIALOG, isAgree);
        editor.apply();
    }

    static boolean getIsAgreeShowDialog(Context context) {
        return getPreferences(context).getBoolean(PREF_KEY_IS_AGREE_SHOW_DIALOG, true);
    }

    public static void setWallpapersArray(Context context, String wallpapersList){
        Editor editor = getPreferencesEditor(context);
        editor.putString(PREF_WALLPAPERS_ARRAY, wallpapersList);
        editor.apply();
    }

    public static String getWallpapersArray(Context context){
        return getPreferences(context).getString(PREF_WALLPAPERS_ARRAY, "");
    }

    static void setRemindInterval(Context context) {
        Editor editor = getPreferencesEditor(context);
        editor.remove(PREF_KEY_REMIND_INTERVAL);
        editor.putLong(PREF_KEY_REMIND_INTERVAL, new Date().getTime());
        editor.apply();
    }

    static long getRemindInterval(Context context) {
        return getPreferences(context).getLong(PREF_KEY_REMIND_INTERVAL, 0);
    }

    static void setInstallDate(Context context) {
        Editor editor = getPreferencesEditor(context);
        editor.putLong(PREF_KEY_INSTALL_DATE, new Date().getTime());
        editor.apply();
    }

    static long getInstallDate(Context context) {
        return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0);
    }

    static void setLaunchTimes(Context context, int launchTimes) {
        Editor editor = getPreferencesEditor(context);
        editor.putInt(PREF_KEY_LAUNCH_TIMES, launchTimes);
        editor.apply();
    }

    public static void setInstallTimes(Context context, int installTimes) {
        Editor editor = getPreferencesEditor(context);
        editor.putInt(PREF_KEY_INSTALL_TIMES, installTimes);
        editor.apply();
    }

    public static int getInstallTimes(Context context) {
        return getPreferences(context).getInt(PREF_KEY_INSTALL_TIMES, 0);
    }

    public static void setAdsShowState(Context context, boolean show) {
        Editor editor = getPreferencesEditor(context);
        editor.putBoolean(PREF_KEY_ADS_STATE, show);
        editor.apply();
    }

    public static boolean isAds(Context context) {
        return getPreferences(context).getBoolean(PREF_KEY_ADS_STATE, false);
    }

    public static void setLimitedDownload(Context context, boolean limited) {
        Editor editor = getPreferencesEditor(context);
        editor.putBoolean(PREF_KEY_IS_LIMITED_DOWNLOAD, limited);
        editor.apply();
    }

    public static boolean isLimitedDownload(Context context) {
        return getPreferences(context).getBoolean(PREF_KEY_IS_LIMITED_DOWNLOAD, false);
    }

    static int getLaunchTimes(Context context) {
        return getPreferences(context).getInt(PREF_KEY_LAUNCH_TIMES, 0);
    }

    public static boolean isFirstLaunch(Context context) {
        return getPreferences(context).getLong(PREF_KEY_INSTALL_DATE, 0) == 0L;
    }

    public static void setInstallInterval(Context context) {
        Editor editor = getPreferencesEditor(context);
        editor.remove(PREF_KEY_INSTALL_INTERVAL);
        editor.putLong(PREF_KEY_INSTALL_INTERVAL, new Date().getTime());
        editor.apply();
    }

    public static long getInstallInterval(Context context) {
        return getPreferences(context).getLong(PREF_KEY_INSTALL_INTERVAL, 0);
    }

}