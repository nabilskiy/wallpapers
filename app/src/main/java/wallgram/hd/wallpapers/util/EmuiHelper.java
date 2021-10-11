package wallgram.hd.wallpapers.util;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.IOException;

public class EmuiHelper {

    public static void setWallpaper(Context context, @Constants.setWallpaperMode int mode, File homeWallpaper,
            File lockWallpaper)
            throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            AppUtils.setWallpaper(context, homeWallpaper);
        } else {
            if (mode == Constants.EXTRA_SET_WALLPAPER_MODE_HOME) {
                AppUtils.setHomeScreenWallpaper(context, homeWallpaper);
            } else if (mode == Constants.EXTRA_SET_WALLPAPER_MODE_LOCK) {
                AppUtils.setLockScreenWallpaper(context, lockWallpaper);
            } else {
                AppUtils.setHomeScreenWallpaper(context, homeWallpaper);
                AppUtils.setLockScreenWallpaper(context, lockWallpaper);
            }
        }
    }

}
