package wallgram.hd.wallpapers.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;
import java.io.File;

public class LockScreenUtil {
    private final Context mContext;

    public LockScreenUtil(Context paramContext) {
        this.mContext = paramContext;
    }

    public Intent getLockScreenIntent(String manufacturer, String paramString) {
        switch (manufacturer.toLowerCase()) {
            case "samsung":
                return getSamsungIntent(paramString);
            case "xiaomi":
                return getXiaomiIntent(paramString);
            default:
                return getOthersIntent(paramString);
        }
    }

    private Intent getOthersIntent(String paramString){
        Intent intent;
        Uri uriPath = getUriWithPath(mContext, paramString);
        ComponentName componentName = new ComponentName("com.android.gallery3d", "com.android.gallery3d.app.Wallpaper");
        intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uriPath, "image/*");
        intent.putExtra("mimeType", "image/*");
        intent.setComponent(componentName);
        return intent;
    }

    private Intent getSamsungIntent(String paramString){
        Intent localIntent = new Intent("android.intent.action.ATTACH_DATA");
        localIntent.setClassName("com.sec.android.gallery3d", "com.sec.android.gallery3d.app.LockScreen");
        localIntent.setDataAndType(Uri.parse("file://" + paramString), "image/*");
        return localIntent;
    }

    private Intent getXiaomiIntent(String paramString) {
        Intent intent;
        Uri uriPath = getUriWithPath(mContext, paramString);
        try {
            ComponentName componentName = new ComponentName("com.android.thememanager",
                    "com.android.thememanager.activity.WallpaperDetailActivity");
            intent = new Intent("miui.intent.action.START_WALLPAPER_DETAIL");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uriPath, "image/*");
            intent.putExtra("mimeType", "image/*");
            intent.setComponent(componentName);
            return intent;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private  Uri getUriWithPath(Context context, String filepath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context.getApplicationContext(), "com.akspic.fileprovider", new File(filepath));
        } else {
            return Uri.fromFile(new File(filepath));
        }
    }
}