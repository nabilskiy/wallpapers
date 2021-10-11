package wallgram.hd.wallpapers.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import wallgram.hd.wallpapers.model.Config;
import wallgram.hd.wallpapers.util.cache.CacheUtils;

public class UIHelper implements IUIHelper {

    @Override
    public Uri setWallpaper(Context context, @NonNull Config config, File wallpaper) throws IOException {
        if (WallpaperUtils.isNotSupportedWallpaper(context)) {
            throw new IOException("This device not support wallpaper");
        }
        File home = new File(wallpaper.toURI());
        File lock = new File(wallpaper.toURI());

        int mode = config.getWallpaperMode();
        if (ROM.getROM().isMiui()) {
            MiuiHelper.setWallpaper(context, mode, home, lock);
        } else if (ROM.getROM().isEmui()) {
            EmuiHelper.setWallpaper(context, mode, home, lock);
        } else if (ROM.getROM().isOneUi()) {
            OneUiHelper.setWallpaper(context, mode, home, lock);
        } else {
            systemSetWallpaper(context, mode, home, lock);
        }
        return Uri.parse(wallpaper.getAbsolutePath());
    }

    private void systemSetWallpaper(Context context, @Constants.setWallpaperMode int mode, File home,
                                    File lock) throws IOException {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            AppUtils.setWallpaper(context, home);
        } else {
            if (mode == Constants.EXTRA_SET_WALLPAPER_MODE_HOME) {
                AppUtils.setHomeScreenWallpaper(context, home);
            } else if (mode == Constants.EXTRA_SET_WALLPAPER_MODE_LOCK) {
                AppUtils.setLockScreenWallpaper(context, lock);
            } else {
                AppUtils.setHomeScreenWallpaper(context, home);
                AppUtils.setLockScreenWallpaper(context, lock);
            }
        }
    }

    public static File cropWallpaper(Context context, File wallpaper) throws IOException {
        return cropWallpaper(context, wallpaper, true);
    }

    public static DisplayMetrics getSysResolution(Context context) {
        return DisplayUtils.getScreenInfo(context, true);
    }

    public static File cropWallpaper(Context context, File wallpaper, boolean portrait) throws IOException {
        DisplayMetrics size = getSysResolution(context);
        int width = size.widthPixels;
        int height = size.heightPixels;
        if (width == 0 || height == 0) {
            throw new IOException("WindowManager is null");
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(wallpaper.getAbsolutePath(), options);
        boolean isCrop = false;
        if (portrait && (width > height)) {//ensure portrait
            int tmp = width;
            width = height;
            height = tmp;
        }
        if (options.outHeight > height) {
            isCrop = true;
        }
        if (isCrop) {
            String key = createKey(wallpaper.getAbsolutePath() + "_thumbnail");
            File wallpaperFile = wallgram.hd.wallpapers.util.cache.CacheUtils.get().get(key);
            if (wallpaperFile == null) {
                Bitmap newBitmap = ThumbnailUtils.extractThumbnail(
                        BitmapFactory.decodeFile(wallpaper.getAbsolutePath()),
                        width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                if (newBitmap != null) {
                    wallpaper = CacheUtils.get().put(key, BitmapUtils.bitmapToStream(newBitmap,
                            Bitmap.CompressFormat.JPEG));
                }
            } else {
                wallpaper = wallpaperFile;
            }
        }
        return wallpaper;
    }

    public static String createKey(String str) {
        return MD5Utils.md5Hex(str).toLowerCase();
    }
}