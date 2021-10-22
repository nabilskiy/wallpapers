package wallgram.hd.wallpapers.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;

public class MiuiHelper {

    @SuppressWarnings({"WeakerAccess"})
    public static void setLockScreenWallpaper(Context context, File wallpaper) throws IOException {
        if (ShellUtils.hasRootPermission()) {
            setImage(UIHelper.cropWallpaper(context, wallpaper, false));
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            if (!ShellUtils.hasRootPermission()) {
                setLockFromIntent(context, wallpaper);
                return;
            }
            setLockScreenWallpaper(context, wallpaper);
        } else {
            AppUtils.setLockScreenWallpaper(context, wallpaper);
        }
    }

    private static void setLockFromIntent(Context context, File wallpaper) throws IOException {

        Uri uriPath = getUriWithPath(context, wallpaper.getAbsolutePath());
        ComponentName componentName = new ComponentName("com.android.thememanager",
                "com.android.thememanager.activity.WallpaperDetailActivity");
        Intent intent = new Intent("miui.intent.action.START_WALLPAPER_DETAIL");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uriPath, "image/*");
        intent.putExtra("mimeType", "image/*");
        intent.setComponent(componentName);
        if (intent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(intent);
        else UIHelper.openDefaultInstaller(context);
    }

    private static Uri getUriWithPath(Context context, String filepath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context.getApplicationContext(), "com.hd.wallpapers.fileprovider", new File(filepath));
        } else {
            return Uri.fromFile(new File(filepath));
        }
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
