package wallgram.hd.wallpapers.util;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;


import java.io.File;
import java.io.IOException;

public class OneUiHelper {

    public static void setWallpaper(Context context, @Constants.setWallpaperMode int mode, File homeWallpaper,
            File lockWallpaper)
            throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            AppUtils.setWallpaper(context, homeWallpaper);
        } else {
            if (mode == Constants.EXTRA_SET_WALLPAPER_MODE_HOME) {
                homeSetWallpaper(context, homeWallpaper);
            } else if (mode == Constants.EXTRA_SET_WALLPAPER_MODE_LOCK) {
                AppUtils.setLockScreenWallpaper(context, lockWallpaper);
            } else {
                homeSetWallpaper(context, homeWallpaper);
                AppUtils.setLockScreenWallpaper(context, lockWallpaper);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static void homeSetWallpaper(Context context, File wallpaper) throws IOException {
        //wallpaper = UIHelper.cropWallpaper(context, wallpaper,false);
        AppUtils.setHomeScreenWallpaper(context, wallpaper);
    }
}
