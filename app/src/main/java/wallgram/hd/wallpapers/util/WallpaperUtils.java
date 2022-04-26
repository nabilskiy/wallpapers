package wallgram.hd.wallpapers.util;

import android.app.WallpaperManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.common.base.Strings;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;


public class WallpaperUtils {

    public static boolean isNotSupportedWallpaper(Context context) {
        try {
            WallpaperManager manager = WallpaperManager.getInstance(context);
            if (manager == null) {
                Toast.makeText(context, "This device not support wallpaper", Toast.LENGTH_LONG).show();
                return true;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!manager.isWallpaperSupported()) {
                    Toast.makeText(context, "This device not support wallpaper", Toast.LENGTH_LONG).show();
                    return true;
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (!manager.isSetWallpaperAllowed()) {
                    Toast.makeText(context, "This device not support set wallpaper", Toast.LENGTH_LONG).show();
                    return true;
                }
            }
        } catch (Throwable ignored) {
        }
        return false;
    }

    private static void saveWallpaper(Context context, String TAG, String imageUrl, File file) {
        try {
            saveToFile(context, imageUrl, file);
            Log.d(TAG, "wallpaper save url: " + imageUrl);
        } catch (IOException e) {
        }
    }

    public static String getName(String fullName) {
        String extension = FileUtils.getExtension(fullName);
        return Files.getNameWithoutExtension(fullName) + (Strings.isNullOrEmpty(extension) ? "" : "." + extension);
    }

    public static Uri saveToFile(Context context, String url, File from) throws IOException {
        String name = getName(url);
        String[] split = name.split("=");
        if (split.length > 1) {
            name = split[1];
        }
        return FileUtils.saveFileToPictureCompat(context, name, from);
    }

    public static File getImageFile(Context context, String url) throws Exception {
        return Glide.with(context).downloadOnly().load(url).timeout(220000).submit().get();
    }


//    public static <T extends View> void loadImage(GlideRequest<File> request, T imageView,
//            Callback<File> callback) {
//        request.addListener(new RequestListener<File>() {
//
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target,
//                    boolean isFirstResource) {
//                if (callback != null) {
//                    callback.onPostExecute();
//                    callback.onError(e);
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(File resource, Object model, Target<File> target,
//                    DataSource dataSource,
//                    boolean isFirstResource) {
//                return false;
//            }
//        }).into(new CustomViewTarget<T, File>(imageView) {
//            @Override
//            public void onLoadFailed(@Nullable Drawable errorDrawable) {
//            }
//
//            @Override
//            public void onResourceReady(@NonNull @NotNull File resource,
//                    @Nullable Transition<? super File> transition) {
//                if (callback != null) {
//                    callback.onPostExecute();
//                    callback.onSuccess(resource);
//                }
//            }
//
//            @Override
//            protected void onResourceCleared(@Nullable Drawable placeholder) {
//            }
//
//            @Override
//            public void onResourceLoading(Drawable placeholder) {
//                super.onResourceLoading(placeholder);
//                if (callback != null) {
//                    callback.onPreExecute();
//                }
//            }
//        });
//    }


}
