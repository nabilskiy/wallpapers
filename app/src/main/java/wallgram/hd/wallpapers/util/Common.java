package wallgram.hd.wallpapers.util;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.util.Locale;

import wallgram.hd.wallpapers.BuildConfig;

public class Common {

    public static String getProjectName(){
        return BuildConfig.APPLICATION_ID;
    }

    public static String getSiteUrl(){
        String locale = Locale.getDefault().getLanguage().toLowerCase();
        switch (locale){
            case "ru":
                return "https://akspic.ru/";
            case "es":
                return "https://es.akspic.com";
            case "de":
                return "https://akspic.com/de";
            case "fr":
                return "https://akspic.com/fr";
            case "zh":
                return "https://akspic.cn";
            default:
                return "https://akspic.com/";
        }
    }

    public static String getResolution(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        if(manager != null)
            manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels + "x" + metrics.heightPixels;
    }

    public static String getLandscapeResolution(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        if(manager != null)
            manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels + "x" + metrics.widthPixels;
    }

    public static String getResolutionReverse(Context context){
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE));
        if(manager != null)
            manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels + "x" + metrics.heightPixels;
    }

    public static int getWidth(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public static int getHeight(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static String getInfo(@Nullable String message){
        return "Device_Model: " + Build.MODEL + "\nOS_Version: " + Build.VERSION.SDK_INT + "\nApp_Version: " + BuildConfig.VERSION_NAME + "\nMessage: " + (message != null ? message : "");
    }
}
