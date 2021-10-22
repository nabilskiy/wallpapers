package wallgram.hd.wallpapers.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.io.File;
import java.io.IOException;

public class EmuiHelper {

    public static void setWallpaper(Context context, @Constants.setWallpaperMode int mode, File homeWallpaper,
            File lockWallpaper)
            throws IOException {
            if (mode == Constants.EXTRA_SET_WALLPAPER_MODE_HOME) {
                homeSetWallpaper(context, homeWallpaper);
            } else if (mode == Constants.EXTRA_SET_WALLPAPER_MODE_LOCK) {
                setLockScreenWallpaper(context, lockWallpaper);
            } else {
                homeSetWallpaper(context, homeWallpaper);
                setLockScreenWallpaper(context, lockWallpaper);
            }
        }

    private static void setLockScreenWallpaper(Context context, File lockWallpaper) throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            lockSetWallpaper(context, lockWallpaper);
        } else {
            AppUtils.setLockScreenWallpaper(context, lockWallpaper);
        }
    }

    private static void lockSetWallpaper(Context context, File wallpaper) throws IOException{
        Intent localIntent = new Intent("android.intent.action.ATTACH_DATA");
        localIntent.setClassName("com.sec.android.gallery3d", "com.sec.android.gallery3d.app.LockScreen");
        localIntent.setDataAndType(Uri.parse("file://" + wallpaper.getAbsolutePath()), "image/*");
        if(localIntent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(localIntent);
        else UIHelper.openDefaultInstaller(context);
    }


    private static void homeSetWallpaper(Context context, File wallpaper) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AppUtils.setHomeScreenWallpaper(context, wallpaper);
        } else {
            AppUtils.setWallpaper(context, wallpaper);
        }
        //wallpaper = UIHelper.cropWallpaper(context, wallpaper,false);
    }

}
