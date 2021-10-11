package wallgram.hd.wallpapers.util;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.IOException;

public class MiuiHelper {

    @SuppressWarnings({ "WeakerAccess" })
    public static void setLockScreenWallpaper(Context context, File wallpaper) throws IOException {
        if (ShellUtils.hasRootPermission()) {
            setImage(UIHelper.cropWallpaper(context, wallpaper,false));
            return;
        }
        throw new LockSetWallpaperException("Not acquired root permission");
    }

    private static void setImage(File file) throws IOException {
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand(
                "cp " + file.getAbsolutePath() + " /data/system/theme/lock_wallpaper", true, true);
        if (commandResult.result == 0) {
            ShellUtils.execCommand("chmod 755 /data/system/theme/lock_wallpaper", true);
        } else {
            throw new IOException("Shell cp error");
        }
    }

    public static void setWallpaper(Context context, @Constants.setWallpaperMode int mode, File homeWallpaper,
            File lockWallpaper)
            throws IOException {
        if (mode == Constants.EXTRA_SET_WALLPAPER_MODE_HOME) {
            homeSetWallpaper(context, homeWallpaper);
        } else if (mode == Constants.EXTRA_SET_WALLPAPER_MODE_LOCK) {
            lockSetWallpaper(context, lockWallpaper);
        } else {
            homeSetWallpaper(context, homeWallpaper);
            try {
                lockSetWallpaper(context, lockWallpaper);
            } catch (Exception ignored) {
            }
        }
    }

    public static void lockSetWallpaper(Context context, File wallpaper) throws IOException {
        if (!ShellUtils.hasRootPermission()) {
            throw new LockSetWallpaperException("Not enable lock screen support");
        }
        setLockScreenWallpaper(context, wallpaper);
    }

    private static void homeSetWallpaper(Context context, File wallpaper) throws IOException {
        //wallpaper = UIHelper.cropWallpaper(context, wallpaper);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AppUtils.setHomeScreenWallpaper(context, wallpaper);
        } else {
            AppUtils.setWallpaper(context, wallpaper);
        }
    }
}
