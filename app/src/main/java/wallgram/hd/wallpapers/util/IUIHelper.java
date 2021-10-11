package wallgram.hd.wallpapers.util;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.IOException;

import wallgram.hd.wallpapers.model.Config;

public interface IUIHelper {
    Uri setWallpaper(Context context, @NonNull Config config, File wallpaper)
            throws IOException;
}