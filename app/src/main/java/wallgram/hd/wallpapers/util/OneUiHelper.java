package wallgram.hd.wallpapers.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class OneUiHelper {

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

        File file = new File(SDCardUtils.getExternalStoragePicturesPublicDirectory(), "Wallgram");

        File outFile = FileUtils.createFile(file, "test.jpg");
        com.google.common.io.Files.copy(wallpaper, outFile);

        localIntent.setDataAndType(Uri.fromFile(outFile), "image/*");
        try{
            context.startActivity(localIntent);
        }
        catch(ActivityNotFoundException exception){
            UIHelper.openDefaultInstaller(context);
        }
    }

    private static Uri getUriWithPath(Context context, String filepath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context.getApplicationContext(), "com.hd.wallpapers.fileprovider", new File(filepath));
        } else {
            return Uri.fromFile(new File(filepath));
        }
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
