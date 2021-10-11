package wallgram.hd.wallpapers.util;

import java.io.IOException;


public class LockSetWallpaperException extends IOException {
    public LockSetWallpaperException() {
    }

    public LockSetWallpaperException(String message) {
        super(message);
    }

    public LockSetWallpaperException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockSetWallpaperException(Throwable cause) {
        super(cause);
    }
}
